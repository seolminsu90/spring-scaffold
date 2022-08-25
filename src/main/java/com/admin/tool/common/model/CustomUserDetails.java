package com.admin.tool.common.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
public class CustomUserDetails implements UserDetails {

    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String pwd;
    private LocalDateTime regDate;

    public CustomUserDetails() {

    }
    public CustomUserDetails(String id, String name, String pwd) {
        this.id = id;
        this.name = name;
        this.pwd = pwd;
    }

    private Collection<? extends GrantedAuthority> authorities;
    private String token;

    @Override
    public String getPassword() {
        return this.pwd;
    }

    @Override
    public String getUsername() {
        return this.id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}