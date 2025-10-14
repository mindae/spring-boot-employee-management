package com.example.hello.utils;

import org.slf4j.Logger;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * Utility class for consistent exception handling patterns across the application.
 * Provides methods for structured exception handling with correlation IDs and context.
 */
public class ExceptionUtils {

    private static final String ERROR_ID_KEY = "errorId";
    private static final String ERROR_TYPE_KEY = "errorType";
    private static final String ERROR_CONTEXT_KEY = "errorContext";

    /**
     * Log exception with structured context
     */
    public static void logException(Logger logger, String operation, Exception exception, String context) {
        String errorId = UUID.randomUUID().toString().substring(0, 8);
        MDC.put(ERROR_ID_KEY, errorId);
        MDC.put(ERROR_TYPE_KEY, exception.getClass().getSimpleName());
        MDC.put(ERROR_CONTEXT_KEY, context);
        
        logger.error("Error in operation: {} - Context: {} - Error ID: {}", 
            operation, context, errorId, exception);
        
        clearErrorContext();
    }

    /**
     * Log exception with user context
     */
    public static void logExceptionWithUser(Logger logger, String operation, Exception exception, 
                                          String context, String userId) {
        String errorId = UUID.randomUUID().toString().substring(0, 8);
        MDC.put(ERROR_ID_KEY, errorId);
        MDC.put(ERROR_TYPE_KEY, exception.getClass().getSimpleName());
        MDC.put(ERROR_CONTEXT_KEY, context);
        MDC.put("userId", userId);
        
        logger.error("Error in operation: {} - User: {} - Context: {} - Error ID: {}", 
            operation, userId, context, errorId, exception);
        
        clearErrorContext();
    }

    /**
     * Log business exception with business context
     */
    public static void logBusinessException(Logger logger, String operation, Exception exception, 
                                         String businessContext, String entityId) {
        String errorId = UUID.randomUUID().toString().substring(0, 8);
        MDC.put(ERROR_ID_KEY, errorId);
        MDC.put(ERROR_TYPE_KEY, exception.getClass().getSimpleName());
        MDC.put(ERROR_CONTEXT_KEY, businessContext);
        MDC.put("entityId", entityId);
        
        logger.error("Business error in operation: {} - Entity: {} - Context: {} - Error ID: {}", 
            operation, entityId, businessContext, errorId, exception);
        
        clearErrorContext();
    }

    /**
     * Log security exception with security context
     */
    public static void logSecurityException(Logger logger, String operation, Exception exception, 
                                          String securityContext, String userAgent, String ipAddress) {
        String errorId = UUID.randomUUID().toString().substring(0, 8);
        MDC.put(ERROR_ID_KEY, errorId);
        MDC.put(ERROR_TYPE_KEY, exception.getClass().getSimpleName());
        MDC.put(ERROR_CONTEXT_KEY, securityContext);
        MDC.put("userAgent", userAgent);
        MDC.put("ipAddress", ipAddress);
        
        logger.error("Security error in operation: {} - IP: {} - Context: {} - Error ID: {}", 
            operation, ipAddress, securityContext, errorId, exception);
        
        clearErrorContext();
    }

    /**
     * Log database exception with database context
     */
    public static void logDatabaseException(Logger logger, String operation, Exception exception, 
                                         String query, String tableName) {
        String errorId = UUID.randomUUID().toString().substring(0, 8);
        MDC.put(ERROR_ID_KEY, errorId);
        MDC.put(ERROR_TYPE_KEY, exception.getClass().getSimpleName());
        MDC.put(ERROR_CONTEXT_KEY, "database");
        MDC.put("query", query);
        MDC.put("tableName", tableName);
        
        logger.error("Database error in operation: {} - Table: {} - Query: {} - Error ID: {}", 
            operation, tableName, query, errorId, exception);
        
        clearErrorContext();
    }

    /**
     * Log external service exception
     */
    public static void logExternalServiceException(Logger logger, String operation, Exception exception, 
                                                 String serviceName, String endpoint, int statusCode) {
        String errorId = UUID.randomUUID().toString().substring(0, 8);
        MDC.put(ERROR_ID_KEY, errorId);
        MDC.put(ERROR_TYPE_KEY, exception.getClass().getSimpleName());
        MDC.put(ERROR_CONTEXT_KEY, "external-service");
        MDC.put("serviceName", serviceName);
        MDC.put("endpoint", endpoint);
        MDC.put("statusCode", String.valueOf(statusCode));
        
        logger.error("External service error in operation: {} - Service: {} - Endpoint: {} - Status: {} - Error ID: {}", 
            operation, serviceName, endpoint, statusCode, errorId, exception);
        
        clearErrorContext();
    }

    /**
     * Create standardized error response
     */
    public static ErrorResponse createErrorResponse(int status, String error, String message, String errorId) {
        return new ErrorResponse(status, error, message, errorId, System.currentTimeMillis());
    }

    /**
     * Create error response with additional context
     */
    public static ErrorResponse createErrorResponse(int status, String error, String message, String errorId, 
                                                  String context, String entityId) {
        return new ErrorResponse(status, error, message, errorId, System.currentTimeMillis(), context, entityId);
    }

    /**
     * Check if exception is retryable
     */
    public static boolean isRetryableException(Throwable throwable) {
        // Network-related exceptions are typically retryable
        return throwable instanceof java.net.ConnectException ||
               throwable instanceof java.net.SocketTimeoutException ||
               throwable instanceof java.net.UnknownHostException ||
               throwable instanceof java.sql.SQLTransientException ||
               throwable instanceof org.springframework.dao.TransientDataAccessException;
    }

    /**
     * Check if exception is a business logic error
     */
    public static boolean isBusinessLogicException(Exception exception) {
        return exception instanceof IllegalArgumentException ||
               exception instanceof IllegalStateException ||
               exception instanceof UnsupportedOperationException;
    }

    /**
     * Check if exception is a security-related error
     */
    public static boolean isSecurityException(Exception exception) {
        return exception instanceof SecurityException ||
               exception instanceof org.springframework.security.access.AccessDeniedException ||
               exception instanceof org.springframework.security.core.AuthenticationException;
    }

    /**
     * Get exception severity level
     */
    public static ExceptionSeverity getExceptionSeverity(Throwable throwable) {
        if (throwable instanceof SecurityException ||
            throwable instanceof org.springframework.security.access.AccessDeniedException ||
            throwable instanceof org.springframework.security.core.AuthenticationException) {
            return ExceptionSeverity.HIGH;
        } else if (throwable instanceof OutOfMemoryError) {
            return ExceptionSeverity.CRITICAL;
        } else if (throwable instanceof IllegalArgumentException ||
                   throwable instanceof IllegalStateException ||
                   throwable instanceof UnsupportedOperationException) {
            return ExceptionSeverity.MEDIUM;
        } else if (isRetryableException(throwable)) {
            return ExceptionSeverity.LOW;
        } else {
            return ExceptionSeverity.MEDIUM;
        }
    }

    /**
     * Clear error context from MDC
     */
    public static void clearErrorContext() {
        MDC.remove(ERROR_ID_KEY);
        MDC.remove(ERROR_TYPE_KEY);
        MDC.remove(ERROR_CONTEXT_KEY);
        MDC.remove("entityId");
        MDC.remove("userAgent");
        MDC.remove("ipAddress");
        MDC.remove("query");
        MDC.remove("tableName");
        MDC.remove("serviceName");
        MDC.remove("endpoint");
        MDC.remove("statusCode");
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
        private final String context;
        private final String entityId;

        public ErrorResponse(int status, String error, String message, String errorId, long timestamp) {
            this(status, error, message, errorId, timestamp, null, null);
        }

        public ErrorResponse(int status, String error, String message, String errorId, long timestamp, 
                           String context, String entityId) {
            this.status = status;
            this.error = error;
            this.message = message;
            this.errorId = errorId;
            this.timestamp = timestamp;
            this.context = context;
            this.entityId = entityId;
        }

        // Getters
        public int getStatus() { return status; }
        public String getError() { return error; }
        public String getMessage() { return message; }
        public String getErrorId() { return errorId; }
        public long getTimestamp() { return timestamp; }
        public String getContext() { return context; }
        public String getEntityId() { return entityId; }
    }

    /**
     * Exception severity levels
     */
    public enum ExceptionSeverity {
        LOW, MEDIUM, HIGH, CRITICAL
    }
}
