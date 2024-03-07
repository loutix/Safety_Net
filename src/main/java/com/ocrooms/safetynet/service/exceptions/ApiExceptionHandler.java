package com.ocrooms.safetynet.service.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;
import java.util.Objects;


@Slf4j
@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<Object> IllegalArgumentException(RuntimeException ex) {
        ApiException apiException = new ApiException(new Date(), ex.getMessage(), ex.getMessage());
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<Object> handleApiRequestException(ItemNotFoundException e) {
        HttpStatus NotFound = HttpStatus.NOT_FOUND;
        ApiException apiException = new ApiException(new Date(), e.getMessage(), NotFound.toString());
        log.error(e.getMessage());
        return new ResponseEntity<>(apiException, NotFound);
    }

    @ExceptionHandler(ItemAlreadyExists.class)
    public ResponseEntity<Object> ItemAlreadyExists(ItemAlreadyExists e) {
        HttpStatus NotFound = HttpStatus.BAD_REQUEST;
        ApiException apiException = new ApiException(new Date(), e.getMessage(), NotFound.toString());
        log.error(e.getMessage());
        return new ResponseEntity<>(apiException, NotFound);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> customValidationErrorHanding(MethodArgumentNotValidException exception) {
        ApiException apiException = new ApiException(new Date(), "Validation Error", Objects.requireNonNull(exception.getBindingResult().getFieldError()).getDefaultMessage());
        log.error("Condition @validate non respectée : " + exception.getBindingResult().getFieldError());
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }


//    private ResponseEntity<ApiException> handleException(Exception exception, HttpStatus status) {
//        ApiException apiException = new ApiException(exception, status);
//        log.error(exception.getMessage(), exception);
//        return new ResponseEntity<>(apiException, status);
//    }


}
