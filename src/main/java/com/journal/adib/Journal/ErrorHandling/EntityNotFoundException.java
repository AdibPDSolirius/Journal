package com.journal.adib.Journal.ErrorHandling;

import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends Exception {

    private String errorMessage;
    private HttpStatus errorCode;

    public EntityNotFoundException(String errorMessage, HttpStatus errorCode){
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public HttpStatus getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(HttpStatus errorCode) {
        this.errorCode = errorCode;
    }
}
