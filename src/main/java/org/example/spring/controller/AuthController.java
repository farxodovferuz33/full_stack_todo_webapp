package org.example.spring.controller;

import org.example.spring.dao.AuthUserDao;
import org.example.spring.dao.UploadsDao;
import org.example.spring.domain.AuthUser;
import org.example.spring.domain.Uploads;
import org.example.spring.dto.UserRegisterDTO;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Controller
public class AuthController {
    private final Path rootPath = Path.of("C:\\Users\\Feruz\\IdeaProjects\\todo_with_springmvc\\src\\main\\resources\\images");
    private final AuthUserDao authUserDao;
    private final PasswordEncoder passwordEncoder;
    private final UploadsDao uploadsDao;

    public AuthController(AuthUserDao authUserDao, PasswordEncoder passwordEncoder, UploadsDao uploadsDao) {
        this.authUserDao = authUserDao;
        this.passwordEncoder = passwordEncoder;
        this.uploadsDao = uploadsDao;
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
    public String register(@ModelAttribute UserRegisterDTO dto) throws IOException {

        AuthUser authUser = AuthUser.builder()
                .username(dto.username())
                .password(passwordEncoder.encode(dto.password()))
                .role("USER")
//                .image(dto.file())
                .build();

        Long save = authUserDao.save(authUser);

        Uploads upload = Uploads.builder()
                .user_id(save)
                .originalName(dto.file().getOriginalFilename())
                .mimeType(dto.file().getContentType())
                .generatedName(UUID.randomUUID() + "." + StringUtils.getFilenameExtension(dto.file().getOriginalFilename()))
                .size(dto.file().getSize()).build();
        uploadsDao.save(upload);
        Files.copy(dto.file().getInputStream(),
                rootPath.resolve(authUser.getUsername()+"."+upload.getMimeType().substring(6)), StandardCopyOption.REPLACE_EXISTING);
        System.out.println(upload.getOriginalName());
        return "redirect:/auth/login";
    }


    @GetMapping("/user/image/{username:.+}")
    public ResponseEntity<Resource> downloadPage(@PathVariable String username) {
        int i = username.indexOf(".");
        String substring = username.substring(0,i);
        Uploads uploads = uploadsDao.findImgByUserName(substring);
        FileSystemResource fileSystemResource = new FileSystemResource(rootPath.resolve(username));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(uploads.getMimeType()))
                .contentLength(uploads.getSize())
//                .header("Content-Disposition", "attachment; filename = " + uploads.getOriginalName())
                .body(fileSystemResource);
    }


}
