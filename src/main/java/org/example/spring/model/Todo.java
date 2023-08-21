package org.example.spring.model;

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
    private String title;
    private String priority;
    private Object createdAt;
}
