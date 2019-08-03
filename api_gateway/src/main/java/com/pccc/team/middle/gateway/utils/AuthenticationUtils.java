package com.pccc.team.middle.gateway.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Component
public class AuthenticationUtils {
    private static Map<String, List<String>> authList = new HashMap<>();
    private static AntPathMatcher antPathMatcher = new AntPathMatcher();
    private static AuthenticationUtils instance = new AuthenticationUtils();

    private AuthenticationUtils(){
        authList.put("/touda/a",Arrays.asList("ROLE_ADMIN","ROLE_USER"));
    }

    public static AuthenticationUtils getInstance() {
        return instance;
    }


    public static boolean checkAuth(Collection<GrantedAuthority> authorities, Route route) {
        SimpleGrantedAuthority authority = (SimpleGrantedAuthority) authorities.iterator().next();
        List<String> roles = authList.get(route.getFullPath());
        return roles.contains(authority.getAuthority());
    }
}
