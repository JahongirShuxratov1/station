package com.example.stations.exceptions;

import com.example.stations.dto.ApiResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalException {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ApiResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("Validation error")
                .errors(errors)
                .build();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ApiResponse handleResourceNotFoundException(ResourceNotFoundException e) {
        String message ="Resource not found";
        return ApiResponse.builder()
                .message(message)
                .status(HttpStatus.NOT_FOUND)
                .build();
    }

    @ExceptionHandler(BadRequestException.class)
    public ApiResponse handleBadRequestException(BadRequestException e) {
        String message = "Bad request exception";
        return ApiResponse.builder()
                .message(message)
                .status(HttpStatus.BAD_REQUEST)
                .build();
    }
}
