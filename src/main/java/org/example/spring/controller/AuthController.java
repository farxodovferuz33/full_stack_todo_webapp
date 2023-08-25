package org.example.spring.controller;

import jakarta.validation.Valid;
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
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
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
    public String registerPage(Model model) {
        model.addAttribute("dto", new UserRegisterDTO());
        return "register";
    }

    @GetMapping("/auth/logout")
    public String logoutPage() {
        return "logout";
    }

    @PostMapping("/auth/register")
    public String register(@Valid @ModelAttribute("dto") UserRegisterDTO dto, BindingResult errors) throws IOException {

        if (errors.hasErrors()) {
            System.out.println(errors);
            return "register";
        }

        AuthUser authUser = AuthUser.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role("USER")
//                .image(dto.file())
                .build();

        Long save = authUserDao.save(authUser);

        Uploads upload = Uploads.builder()
                .user_id(save)
                .originalName(dto.getFile().getOriginalFilename())
                .mimeType(dto.getFile().getContentType())
                .generatedName(UUID.randomUUID() + "." + StringUtils.getFilenameExtension(dto.getFile().getOriginalFilename()))
                .size(dto.getFile().getSize()).build();
        uploadsDao.save(upload);
        Files.copy(dto.getFile().getInputStream(),
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
