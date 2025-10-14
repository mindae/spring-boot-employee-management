package com.example.hello.utils;

import org.slf4j.Logger;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * Utility class for consistent logging patterns across the application.
 * Provides methods for structured logging with correlation IDs and performance tracking.
 */
public class LoggingUtils {

    private static final String CORRELATION_ID_KEY = "correlationId";
    private static final String USER_ID_KEY = "userId";
    private static final String OPERATION_KEY = "operation";

    /**
     * Start a new operation with correlation ID
     */
    public static void startOperation(Logger logger, String operation, String... params) {
        String correlationId = UUID.randomUUID().toString().substring(0, 8);
        MDC.put(CORRELATION_ID_KEY, correlationId);
        MDC.put(OPERATION_KEY, operation);
        
        if (params.length > 0) {
            logger.info("Starting operation: {} with params: {}", operation, String.join(", ", params));
        } else {
            logger.info("Starting operation: {}", operation);
        }
    }

    /**
     * End an operation with execution time
     */
    public static void endOperation(Logger logger, String operation, long startTime) {
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Completed operation: {} in {}ms", operation, duration);
        clearMDC();
    }

    /**
     * Log an error with context
     */
    public static void logError(Logger logger, String operation, String message, Throwable throwable) {
        logger.error("Error in operation: {} - {}", operation, message, throwable);
    }

    /**
     * Log performance metrics
     */
    public static void logPerformance(Logger logger, String operation, long duration, String metric) {
        if (duration > 1000) { // Log slow operations
            logger.warn("Slow operation detected: {} took {}ms for {}", operation, duration, metric);
        } else {
            logger.debug("Operation: {} completed in {}ms for {}", operation, duration, metric);
        }
    }

    /**
     * Set user context for logging
     */
    public static void setUserContext(String userId) {
        MDC.put(USER_ID_KEY, userId);
    }

    /**
     * Clear user context
     */
    public static void clearUserContext() {
        MDC.remove(USER_ID_KEY);
    }

    /**
     * Clear all MDC context
     */
    public static void clearMDC() {
        MDC.clear();
    }

    /**
     * Log database operation with timing
     */
    public static void logDatabaseOperation(Logger logger, String operation, String table, long duration) {
        logger.debug("Database operation: {} on table: {} took {}ms", operation, table, duration);
        
        if (duration > 500) { // Log slow database operations
            logger.warn("Slow database operation: {} on table: {} took {}ms", operation, table, duration);
        }
    }

    /**
     * Log HTTP request/response
     */
    public static void logHttpRequest(Logger logger, String method, String uri, int statusCode, long duration) {
        logger.info("HTTP {} {} - Status: {} - Duration: {}ms", method, uri, statusCode, duration);
        
        if (statusCode >= 400) {
            logger.warn("HTTP Error: {} {} - Status: {} - Duration: {}ms", method, uri, statusCode, duration);
        }
    }

    /**
     * Log external API calls
     */
    public static void logExternalApiCall(Logger logger, String apiName, String endpoint, int statusCode, long duration) {
        logger.info("External API call: {} to {} - Status: {} - Duration: {}ms", apiName, endpoint, statusCode, duration);
        
        if (statusCode >= 400) {
            logger.error("External API error: {} to {} - Status: {} - Duration: {}ms", apiName, endpoint, statusCode, duration);
        }
    }
}
