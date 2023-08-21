package org.example.spring.dto;

import org.example.spring.enums.Role;

public record UserRegisterDTO(String username, String password, String role) {
}
