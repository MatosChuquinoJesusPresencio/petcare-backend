package com.petcare.backend.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import com.petcare.backend.domain.exception.BusinessRuleException;
import com.petcare.backend.domain.exception.ResourceDuplicateException;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import com.petcare.backend.domain.exception.ScheduleConflictException;
import com.petcare.backend.domain.exception.TokenRefreshException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Error de Validación");

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        response.put("mensaje", errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Recurso no encontrado", ex.getMessage());
    }

    @ExceptionHandler(ResourceDuplicateException.class)
    public ResponseEntity<Map<String, Object>> handleResourceDuplicateException(ResourceDuplicateException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, "Conflicto de duplicidad", ex.getMessage());
    }

    @ExceptionHandler(ScheduleConflictException.class)
    public ResponseEntity<Map<String, Object>> handleScheduleConflictException(ScheduleConflictException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, "Conflicto de horario", ex.getMessage());
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessRuleException(BusinessRuleException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Regla de negocio no cumplida", ex.getMessage());
    }

    @ExceptionHandler(TokenRefreshException.class)
    public ResponseEntity<Map<String, Object>> handleTokenRefreshException(TokenRefreshException ex) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, "Error de Refresh Token", ex.getMessage());
    }

    // Mantener IllegalArgumentException como respaldo por si alguna biblioteca la lanza
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Solicitud Incorrecta", ex.getMessage());
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String error, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", status.value());
        response.put("error", error);
        response.put("mensaje", message);
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("error", "Error Interno del Servidor");
        response.put("mensaje", ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
