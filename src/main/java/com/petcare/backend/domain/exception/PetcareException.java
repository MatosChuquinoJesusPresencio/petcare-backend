package com.petcare.backend.domain.exception;

public class PetcareException extends RuntimeException {
    public PetcareException(String message) {
        super(message);
    }

    public PetcareException(String message, Throwable cause) {
        super(message, cause);
    }
}
