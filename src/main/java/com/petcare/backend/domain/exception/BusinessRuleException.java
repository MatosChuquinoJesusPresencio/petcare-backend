package com.petcare.backend.domain.exception;

public class BusinessRuleException extends PetcareException {
    public BusinessRuleException(String message) {
        super(message);
    }

    public BusinessRuleException(String message, Throwable cause) {
        super(message, cause);
    }
}
