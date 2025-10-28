package dev.matheuslf.desafio.inscritos.utils;

import dev.matheuslf.desafio.inscritos.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ErrorBuilder {

    public ErrorBuilder() {
    }

    public static ResponseEntity<ErrorResponse> build(HttpStatus status, String message, String path) {
        ErrorResponse error = ErrorResponse.from(status, message, path);
        return ResponseEntity.status(status).body(error);
    }

    public static ResponseEntity<ErrorResponse> build(HttpStatus status, String message, String path, List<String> validationError) {
        ErrorResponse error = ErrorResponse.from(status, message, path, validationError);
        return ResponseEntity.status(status).body(error);
    }

}