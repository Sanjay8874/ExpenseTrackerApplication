package com.expense.ExpenseTrackerApplication.Controller;

// package: com.expense.shared.error
import com.expense.ExpenseTrackerApplication.Exception.ApiError;
import com.expense.ExpenseTrackerApplication.Exception.BadRequestException;
import com.expense.ExpenseTrackerApplication.Exception.DuplicateResourceException;
import com.expense.ExpenseTrackerApplication.Exception.ResourceNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException ex, jakarta.servlet.http.HttpServletRequest req) {
        return ResponseEntity.status(NOT_FOUND)
                .body(ApiError.of(404, "Not Found", ex.getMessage(), req.getRequestURI(), "RESOURCE_NOT_FOUND", null));
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiError> handleDuplicate(DuplicateResourceException ex, jakarta.servlet.http.HttpServletRequest req) {
        return ResponseEntity.status(CONFLICT)
                .body(ApiError.of(409, "Conflict", ex.getMessage(), req.getRequestURI(), "DUPLICATE_RESOURCE", null));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequest(BadRequestException ex, jakarta.servlet.http.HttpServletRequest req) {
        return ResponseEntity.status(BAD_REQUEST)
                .body(ApiError.of(400, "Bad Request", ex.getMessage(), req.getRequestURI(), "BAD_REQUEST", null));
    }

    // Bean Validation @Valid on DTOs (body)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, jakarta.servlet.http.HttpServletRequest req) {
        Map<String, Object> details = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err -> details.put(err.getField(), err.getDefaultMessage()));
        return ResponseEntity.status(UNPROCESSABLE_ENTITY)
                .body(ApiError.of(422, "Unprocessable Entity", "Validation failed", req.getRequestURI(), "VALIDATION_ERROR", details));
    }

    // Validation on query/path params
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex, jakarta.servlet.http.HttpServletRequest req) {
        Map<String, Object> details = new HashMap<>();
        ex.getConstraintViolations().forEach(v -> details.put(v.getPropertyPath().toString(), v.getMessage()));
        return ResponseEntity.status(UNPROCESSABLE_ENTITY)
                .body(ApiError.of(422, "Unprocessable Entity", "Validation failed", req.getRequestURI(), "VALIDATION_ERROR", details));
    }

    // Type mismatches like ?month=abc
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException ex, jakarta.servlet.http.HttpServletRequest req) {
        return ResponseEntity.status(BAD_REQUEST)
                .body(ApiError.of(400, "Bad Request", "Invalid parameter: " + ex.getName(), req.getRequestURI(), "TYPE_MISMATCH", null));
    }

    // 404 for unmapped endpoints (Spring Boot 3)
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiError> handleNoResource(NoResourceFoundException ex, jakarta.servlet.http.HttpServletRequest req) {
        return ResponseEntity.status(NOT_FOUND)
                .body(ApiError.of(404, "Not Found", "Endpoint not found", req.getRequestURI(), "ENDPOINT_NOT_FOUND", null));
    }

    // Security: forbidden/unauthorized
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(AccessDeniedException ex, jakarta.servlet.http.HttpServletRequest req) {
        return ResponseEntity.status(FORBIDDEN)
                .body(ApiError.of(403, "Forbidden", "You don't have permission to access this resource", req.getRequestURI(), "ACCESS_DENIED", null));
    }

    // Fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAny(Exception ex, jakarta.servlet.http.HttpServletRequest req) {
        // log stacktrace with a correlation id (add MDC if you like)
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(ApiError.of(500, "Internal Server Error", "Something went wrong", req.getRequestURI(), "INTERNAL_ERROR", null));
    }
}

