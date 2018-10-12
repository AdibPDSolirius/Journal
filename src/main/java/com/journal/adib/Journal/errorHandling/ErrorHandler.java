package com.journal.adib.Journal.errorHandling;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandler{

    @ExceptionHandler({JournalException.class})
    public ResponseEntity<Object> entityNotFound(JournalException je) {
        return ResponseEntity.status(je.getErrorCode()).body(je.getErrorMessage());
    }

}