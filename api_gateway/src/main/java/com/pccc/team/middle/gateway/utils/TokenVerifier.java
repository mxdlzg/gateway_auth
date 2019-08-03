package com.pccc.team.middle.gateway.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class TokenVerifier {
    @Autowired
    RedisTemplate<String,String> redisTemplate;
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    public boolean verifyToken(Object accessToken) {
        String key = ((String) accessToken).replace("Bearer ","");
        return stringRedisTemplate.hasKey(key);
    }
}
