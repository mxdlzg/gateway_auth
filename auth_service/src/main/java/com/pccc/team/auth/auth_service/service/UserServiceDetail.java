package com.pccc.team.auth.auth_service.service;

import com.pccc.team.auth.auth_service.entity.JwtUser;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;

@Service
public class UserServiceDetail implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        //TODO::Call touda user service
        return callToudaUserValidation(s);
    }

    private JwtUser callToudaUserValidation(String username){
        JwtUser jwtUser = new JwtUser();
        jwtUser.setId(0);
        jwtUser.setPassword("$2a$10$4pB8RAmbbB45P.P9oT.3c.hC/HPDW9UjTT0NNiFqWMQGQxwaE5uq6");
        jwtUser.setUsername("admin");
        jwtUser.setAuthorities(Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN")));
        return jwtUser;
    }
}
