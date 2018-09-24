package com.journal.adib.Journal.ErrorHandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.journal.adib.Journal.ErrorHandling.EntityNotFoundException;

@ControllerAdvice
public class ErrorHandler{

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<Object> entityNotFound(EntityNotFoundException enfe) {
        return ResponseEntity.status(enfe.getErrorCode()).body(enfe.getErrorMessage());
    }

}