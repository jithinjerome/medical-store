package com.example.medical.store.Exceptions;

import org.springframework.http.HttpStatus;

public class MedicalStoreException extends RuntimeException {
    private final HttpStatus status;

    public MedicalStoreException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
