package com.petcare.backend.domain.exception;

public class ResourceDuplicateException extends PetcareException {
    public ResourceDuplicateException(String message) {
        super(message);
    }

    public ResourceDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }
}
