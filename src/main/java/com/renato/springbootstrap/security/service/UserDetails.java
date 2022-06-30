package com.renato.springbootstrap.security.service;

import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Value
@EqualsAndHashCode(callSuper = false)
public class UserDetails extends User {
    String email;
    List<String> roles;

    public UserDetails(String username, String password, List<SimpleGrantedAuthority> authorities, List<String> roles, String email) {
        super(username, password, authorities);
        this.email = email;
        this.roles = roles;
    }
}
