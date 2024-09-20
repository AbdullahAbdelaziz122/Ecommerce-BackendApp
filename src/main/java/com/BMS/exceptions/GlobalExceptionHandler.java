package com.BMS.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceAlreadyExist.class)
    public ResponseEntity<String> handleResourceAlreadyExist(ResourceAlreadyExist ex) {
        // Return a 409 Conflict status with the exception message
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
