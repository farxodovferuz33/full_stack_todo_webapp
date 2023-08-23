package org.example.spring.dto;

import org.springframework.web.multipart.MultipartFile;

public record UserRegisterDTO(String username, String password, String role, MultipartFile file) {
}
