package org.example.spring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@Getter
@Setter
public class UserRegisterDTO {
    @NotBlank(message = "Username Cannot be Blank")
    String username;

    @NotBlank(message = "Password Cannot be Blank")
    String password;

    String role;

//    @NotBlank(message = "Password Cannot be Blank")
    MultipartFile file;
}
