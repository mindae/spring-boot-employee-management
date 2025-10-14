package com.example.hello.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * AOP Aspect for global exception handling across the application.
 * Provides centralized error handling without manual try-catch blocks.
 */
@Aspect
@Component
public class GlobalExceptionAspect {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionAspect.class);

    /**
     * Pointcut for all controller methods
     */
    @Pointcut("execution(* com.example.hello.controller.*.*(..))")
    public void controllerMethods() {}

    /**
     * Pointcut for all service methods
     */
    @Pointcut("execution(* com.example.hello.service.*.*(..))")
    public void serviceMethods() {}

    /**
     * Pointcut for all repository methods
     */
    @Pointcut("execution(* com.example.hello.repository.*.*(..))")
    public void repositoryMethods() {}

    /**
     * Around advice for controller methods - handles HTTP exceptions
     */
    @Around("controllerMethods()")
    public Object handleControllerExceptions(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String errorId = UUID.randomUUID().toString().substring(0, 8);
        
        MDC.put("errorId", errorId);
        MDC.put("component", "controller");
        MDC.put("className", className);
        MDC.put("methodName", methodName);

        try {
            Object result = joinPoint.proceed();
            return result;
        } catch (IllegalArgumentException e) {
            logger.error("Validation error in {}.{}: {}", className, methodName, e.getMessage(), e);
            return createErrorResponse(HttpStatus.BAD_REQUEST, "Validation Error", e.getMessage(), errorId);
        } catch (IllegalStateException e) {
            logger.error("Business logic error in {}.{}: {}", className, methodName, e.getMessage(), e);
            return createErrorResponse(HttpStatus.CONFLICT, "Business Logic Error", e.getMessage(), errorId);
        } catch (SecurityException e) {
            logger.error("Security error in {}.{}: {}", className, methodName, e.getMessage(), e);
            return createErrorResponse(HttpStatus.FORBIDDEN, "Security Error", e.getMessage(), errorId);
        } catch (RuntimeException e) {
            logger.error("Runtime error in {}.{}: {}", className, methodName, e.getMessage(), e);
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", 
                "An unexpected error occurred", errorId);
        } catch (Exception e) {
            logger.error("Unexpected error in {}.{}: {}", className, methodName, e.getMessage(), e);
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", 
                "An unexpected error occurred", errorId);
        } finally {
            MDC.clear();
        }
    }

    /**
     * Around advice for service methods - handles business exceptions
     */
    @Around("serviceMethods()")
    public Object handleServiceExceptions(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String errorId = UUID.randomUUID().toString().substring(0, 8);
        
        MDC.put("errorId", errorId);
        MDC.put("component", "service");
        MDC.put("className", className);
        MDC.put("methodName", methodName);

        try {
            Object result = joinPoint.proceed();
            return result;
        } catch (IllegalArgumentException e) {
            logger.error("Business validation error in {}.{}: {}", className, methodName, e.getMessage(), e);
            throw new BusinessValidationException("Validation failed: " + e.getMessage(), e);
        } catch (IllegalStateException e) {
            logger.error("Business logic error in {}.{}: {}", className, methodName, e.getMessage(), e);
            throw new BusinessLogicException("Business logic error: " + e.getMessage(), e);
        } catch (RuntimeException e) {
            logger.error("Service error in {}.{}: {}", className, methodName, e.getMessage(), e);
            throw new ServiceException("Service error: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected service error in {}.{}: {}", className, methodName, e.getMessage(), e);
            throw new ServiceException("Unexpected service error: " + e.getMessage(), e);
        } finally {
            MDC.clear();
        }
    }

    /**
     * Around advice for repository methods - handles data access exceptions
     */
    @Around("repositoryMethods()")
    public Object handleRepositoryExceptions(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String errorId = UUID.randomUUID().toString().substring(0, 8);
        
        MDC.put("errorId", errorId);
        MDC.put("component", "repository");
        MDC.put("className", className);
        MDC.put("methodName", methodName);

        try {
            Object result = joinPoint.proceed();
            return result;
        } catch (RuntimeException e) {
            logger.error("Data access error in {}.{}: {}", className, methodName, e.getMessage(), e);
            throw new DataAccessException("Data access error: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected data access error in {}.{}: {}", className, methodName, e.getMessage(), e);
            throw new DataAccessException("Unexpected data access error: " + e.getMessage(), e);
        } finally {
            MDC.clear();
        }
    }

    /**
     * Create standardized error response
     */
    private ResponseEntity<Object> createErrorResponse(HttpStatus status, String error, String message, String errorId) {
        ErrorResponse errorResponse = new ErrorResponse(
            status.value(),
            error,
            message,
            errorId,
            System.currentTimeMillis()
        );
        return ResponseEntity.status(status).body(errorResponse);
    }

    /**
     * Standard error response structure
     */
    public static class ErrorResponse {
        private final int status;
        private final String error;
        private final String message;
        private final String errorId;
        private final long timestamp;

        public ErrorResponse(int status, String error, String message, String errorId, long timestamp) {
            this.status = status;
            this.error = error;
            this.message = message;
            this.errorId = errorId;
            this.timestamp = timestamp;
        }

        // Getters
        public int getStatus() { return status; }
        public String getError() { return error; }
        public String getMessage() { return message; }
        public String getErrorId() { return errorId; }
        public long getTimestamp() { return timestamp; }
    }

    /**
     * Custom business exceptions
     */
    public static class BusinessValidationException extends RuntimeException {
        public BusinessValidationException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class BusinessLogicException extends RuntimeException {
        public BusinessLogicException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class ServiceException extends RuntimeException {
        public ServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class DataAccessException extends RuntimeException {
        public DataAccessException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
