package com.petcare.backend.domain.exception;

public class ResourceNotFoundException extends PetcareException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
