package com.pccc.team.middle.gateway.filter;

import com.google.gson.Gson;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.pccc.team.middle.gateway.VO.ResultVO;
import com.pccc.team.middle.gateway.consts.RedisConsts;
import com.pccc.team.middle.gateway.utils.AuthenticationUtils;
import com.pccc.team.middle.gateway.utils.CookieUtils;
import com.pccc.team.middle.gateway.utils.TokenVerifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_DECORATION_FILTER_ORDER;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * 权限验证 Filter
 * 注册和登录接口不过滤
 * 此过滤器在ResourceServerFilter之后，先鉴权OAuth2
 *
 * 验证权限需要前端在 Cookie 或 Header 中（二选一即可）设置用户的 userId 和 token
 * 因为 token 是存在 Redis 中的，Redis 的键由 userId 构成，值是 token
 * 在两个地方都没有找打 userId 或 token其中之一，就会返回 401 无权限，并给与文字提示
 */
@Slf4j
@Component
public class AuthFilter extends AbstractRouteFilter {

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    RedisTemplate redisTemplate;

    //排除过滤的 uri 地址
    private static final String LOGIN_URI = "/user/login";
    private static final String REGISTER_URI = "/user/register";
    private static final String GET_ROLES_URI = "/user/get-roles";
    private static final String GET_EXPERIENCES_URI = "/user/get-experiences";

    private static final String[] IGNORE_URIS = {
            "/auth/oauth/token",
    };


    //无权限时的提示语
    private static final String INVALID_TOKEN = "invalid token";
    private static final String INVALID_USERID = "invalid userId";

    public AuthFilter(RouteLocator routeLocator, UrlPathHelper urlPathHelper) {
        super(routeLocator,urlPathHelper);
    }

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return PRE_DECORATION_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        log.info("uri:{}", request.getRequestURI());

        //IGNORE_URIS 中的接口不拦截，其他接口都要拦截校验 token
        List<String> uris = Arrays.asList(IGNORE_URIS);
        for (String uri : uris) {
            if (uri.equals(request.getRequestURI())){
                return false;
            }
        }

        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        Object accessToken = request.getHeader("Authorization");
        if (accessToken==null){
            log.warn("Authorization token is empty");
            setUnauthorizedResponse(requestContext,401,"Authorization token is empty!");
            return null;
        }
        if (verifyToken(accessToken)){
            log.info("Authorization token is ok");
            OAuth2Authentication auth = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
            //权限判断
            boolean isPermitted = AuthenticationUtils.checkAuth(auth.getAuthorities(),route(requestContext.getRequest()));
            if (!isPermitted){
                setUnauthorizedResponse(requestContext,403,"This user has no authority to access this API!");
            }
        }else {
            setUnauthorizedResponse(requestContext,401,"Token was expired");
        }
        return null;
    }

    public boolean verifyToken(Object accessToken) {
        String key = ((String) accessToken).replace("Bearer ","access:");
        return stringRedisTemplate.hasKey(key);
    }

    /**
     * 从Redis中校验token
     *
     * @param token
     * @return
     */
    private void verifyToken(RequestContext requestContext, HttpServletRequest request, String token) {
        //需要从cookie或header 中取出 userId 来校验 token 的有效性，因为每个用户对应一个token，在Redis中是以 TOKEN_userId 为键的
        Cookie userIdCookie = CookieUtils.getCookieByName(request, "userId");
        if (userIdCookie == null || StringUtils.isEmpty(userIdCookie.getValue())) {
            //从header中取userId
            String userId = request.getHeader("userId");
            if (StringUtils.isEmpty(userId)) {
                setUnauthorizedResponse(requestContext, INVALID_USERID);
            } else {
                String redisToken = stringRedisTemplate.opsForValue().get(String.format(RedisConsts.TOKEN_TEMPLATE, userId));
                if (StringUtils.isEmpty(redisToken) || !redisToken.equals(token)) {
                    setUnauthorizedResponse(requestContext, INVALID_TOKEN);
                }
            }
        } else {
            String redisToken = stringRedisTemplate.opsForValue().get(String.format(RedisConsts.TOKEN_TEMPLATE, userIdCookie.getValue()));
            if (StringUtils.isEmpty(redisToken) || !redisToken.equals(token)) {
                setUnauthorizedResponse(requestContext, INVALID_TOKEN);
            }
        }
    }

    private void setUnauthorizedResponse(RequestContext requestContext, String msg) {
        requestContext.setSendZuulResponse(false);
        requestContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());

        ResultVO vo = new ResultVO();
        vo.setCode(401);
        vo.setMsg(msg);
        Gson gson = new Gson();
        String result = gson.toJson(vo);

        requestContext.setResponseBody(result);
    }

    /**
     * 设置 401 无权限状态
     *
     * @param requestContext
     */
    private void setUnauthorizedResponse(RequestContext requestContext,int code, String msg) {
        requestContext.setSendZuulResponse(false);
        requestContext.setResponseStatusCode(code);

        ResultVO vo = new ResultVO();
        vo.setCode(code);
        vo.setMsg(msg);
        Gson gson = new Gson();
        String result = gson.toJson(vo);

        requestContext.setResponseBody(result);
    }

}
