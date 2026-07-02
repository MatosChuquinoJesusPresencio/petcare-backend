package com.petcare.backend.domain.exception;

public class ScheduleConflictException extends PetcareException {
    public ScheduleConflictException(String message) {
        super(message);
    }

    public ScheduleConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
