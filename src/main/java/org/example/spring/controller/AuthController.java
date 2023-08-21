package org.example.spring.controller;

import org.example.spring.dao.AuthUserDao;
import org.example.spring.domain.AuthUser;
import org.example.spring.dto.UserRegisterDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    private final AuthUserDao authUserDao;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthUserDao authUserDao, PasswordEncoder passwordEncoder) {
        this.authUserDao = authUserDao;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/auth/login")
    public String login() {
        return "login";
    }

    @GetMapping("/auth/register")
    public String registerPage() {
        return "register";
    }

    @GetMapping("/auth/logout")
    public String logoutPage() {
        return "logout";
    }

    @PostMapping("/auth/register")
    public String register(@ModelAttribute UserRegisterDTO dto) {
        AuthUser authUser = AuthUser.builder()
                .username(dto.username())
                .password(passwordEncoder.encode(dto.password()))
                .role("USER")
                .build();
        authUserDao.save(authUser);
        return "redirect:/auth/login";
    }


}
