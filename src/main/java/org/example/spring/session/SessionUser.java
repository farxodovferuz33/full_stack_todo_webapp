package org.example.spring.session;

import org.example.spring.config.security.CustomUserDetails;
import org.example.spring.domain.AuthUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class SessionUser {
    public AuthUser getUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
//        if (authentication.getPrincipal() instanceof CustomUserDetails userDetails)
//            return userDetails.getAuthUser();
//        return ;
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        return principal.getAuthUser();
    }

    public Long getId() {
        return getUser().getId();
    }
}
