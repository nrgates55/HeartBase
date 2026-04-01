package com.heartbase.heartbase.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Value("${app.security.username}")
    private String username;

    @Value("${app.security.password-hash}")
    private String passwordHash;

    @Override
    public UserDetails loadUserByUsername(String inputUsername) throws UsernameNotFoundException {
        if (!username.equals(inputUsername)) {
            throw new UsernameNotFoundException("User not found");
        }

        return new User(
                username,
                passwordHash,
                AuthorityUtils.createAuthorityList("ROLE_USER")
        );
    }
}