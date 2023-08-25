package org.example.spring.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException{
    private final String path;
    public NotFoundException(String message, String path) {
        super(message);
        this.path = path;
    }
}
