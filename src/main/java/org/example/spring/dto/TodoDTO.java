package org.example.spring.dto;

import jakarta.validation.constraints.NotBlank;

public class TodoDTO {
    @NotBlank(message = "Title cannot be blank")
    private String title;
    @NotBlank(message = "Priority cannot be blank")
    private String priority;
}
