package org.example.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Uploads {
    private Integer id;
    private Long user_id;
    private String originalName;
    private String generatedName;
    private String mimeType;
    private long size;
}
