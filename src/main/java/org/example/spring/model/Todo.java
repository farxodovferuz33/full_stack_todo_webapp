package org.example.spring.model;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Todo {
    private Integer id;
    private String title;
    private String priority;
    private LocalDateTime createdAt;
}
