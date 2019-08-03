package com.pccc.team.middle.gateway.config;

import com.netflix.zuul.ZuulFilter;
import com.pccc.team.middle.gateway.filter.AuthFilter;
import com.pccc.team.middle.gateway.filter.RouteTimesFilter;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UrlPathHelper;

@Component
public class ZuulFilterConfig {
    @Bean
    public ZuulFilter routeTimesFilter(RouteLocator routeLocator){
        return new AuthFilter(routeLocator,new UrlPathHelper());
    }
}
