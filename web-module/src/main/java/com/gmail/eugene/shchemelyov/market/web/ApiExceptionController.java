package com.gmail.eugene.shchemelyov.market.web;

import com.gmail.eugene.shchemelyov.market.repository.exception.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ApiExceptionController {
    private static final Logger logger = LoggerFactory.getLogger(ApiExceptionController.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @SuppressWarnings(value = "unchecked")
    public ResponseEntity processValidationError(MethodArgumentNotValidException e) {
        List<FieldError> errors = e.getBindingResult().getFieldErrors();
        List<String> errorsList = errors.stream()
                .map(error -> String.format("Field: %s - %s.", error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ResponseEntity(errorsList, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExpectedException.class)
    @SuppressWarnings(value = "unchecked")
    public ResponseEntity expectedError(ExpectedException e) {
        logger.error(e.getMessage(), e);
        return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    @SuppressWarnings(value = "unchecked")
    public ResponseEntity defaultError(Exception e) {
        logger.error(e.getMessage(), e);
        return new ResponseEntity("Something went wrong!", HttpStatus.BAD_REQUEST);
    }
}
