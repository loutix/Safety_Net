package com.ocrooms.safetynet.service.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
@AllArgsConstructor
public class ApiException {
    private final Date timeStamp;
    private final String message;
    private final String details;

    public ApiException(Exception exception, HttpStatus status) {
        timeStamp = new Date();
        message = exception.getMessage();
        details = status.getReasonPhrase();
    }
}
