package org.example.spring.domain;

import lombok.*;
import lombok.extern.java.Log;
import org.example.spring.enums.Role;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AuthUser {
    private Long id;
    private String username;
    private String password;
    private String role;
}
