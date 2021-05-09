package com.factorypal.speedmetrics.exceptions;

import org.springframework.http.HttpStatus;

public class FactoryPalException extends RuntimeException {

    HttpStatus status;

    public FactoryPalException(String message) {
        super(message);
    }

    public FactoryPalException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
