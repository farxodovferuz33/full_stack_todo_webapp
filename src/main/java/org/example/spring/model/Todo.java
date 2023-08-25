package org.example.spring.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Todo {
    private Integer id;
    private Long user_id;

    @NotBlank(message = "Title cannot be blank")
    private String title;
    @NotBlank(message = "Priority cannot be blank")
    private String priority;
    private Object createdAt;
}
