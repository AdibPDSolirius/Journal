package com.journal.adib.Journal.ErrorHandling;

import org.springframework.http.HttpStatus;

public class JournalException extends Exception {

    private HttpStatus errorCode;

    private String errorMessage;

    public JournalException(String errorMessage, HttpStatus errorCode){
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public HttpStatus getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(HttpStatus errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
