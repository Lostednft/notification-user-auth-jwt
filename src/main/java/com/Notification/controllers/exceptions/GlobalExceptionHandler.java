package com.Notification.controllers.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity userNullException(NullPointerException nullPointerException){
        return new ResponseEntity(nullPointerException.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity usernameInvalidException(UsernameNotFoundException message){
        return new ResponseEntity(message.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity passwordInvalidException(BadCredentialsException message){
        return new ResponseEntity(message.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity handlerNotFound(NoSuchElementException noSuchException) {
        return new ResponseEntity(noSuchException.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity dateTimeException(){
        String message = "fill all fields correctly.";
        return new ResponseEntity(message, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity unexpectedException(Throwable throwable) {
        String message = "something unexpected happened, see the logs";
        logger.error(message, throwable);
        return new ResponseEntity(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
