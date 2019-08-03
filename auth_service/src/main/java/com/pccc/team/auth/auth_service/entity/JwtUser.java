package com.pccc.team.auth.auth_service.entity;

import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;

@Setter
public class JwtUser implements UserDetails, Serializable {
    private Integer id;
    private String username;
    private String password;
    private String currentAuthority;
    private Collection<? extends GrantedAuthority> authorities;


//    public JwtUser(RtUserEntity userBean) {
//        id = userBean.getId();
//        username = userBean.getUsername();
//        password = userBean.getPassword();
//        authorities = Collections.singleton(new SimpleGrantedAuthority(userBean.getRole()));
//    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    // TODO: 2019/3/3 记录以下信息到数据库
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Integer getId() {
        return id;
    }
}
