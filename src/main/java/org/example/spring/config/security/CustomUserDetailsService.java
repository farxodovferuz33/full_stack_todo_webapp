package org.example.spring.config.security;

import org.example.spring.dao.AuthUserDao;
import org.example.spring.domain.AuthUser;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final AuthUserDao authUserDao;

    public CustomUserDetailsService(AuthUserDao authUserDao) {
        this.authUserDao = authUserDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser authUser = authUserDao.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("Username not found {%s}".formatted(username))
        );
        return new CustomUserDetails(authUser);
    }
}
