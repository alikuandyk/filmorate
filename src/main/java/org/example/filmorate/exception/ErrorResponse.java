package org.example.filmorate.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ErrorResponse {
    private final String error;
    private final HttpStatus status;
}
