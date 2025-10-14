package com.example.hello.aspect;

import com.example.hello.annotation.HandleException;
import com.example.hello.annotation.RetryOnFailure;
import com.example.hello.annotation.Timeout;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * AOP Aspect for handling custom exception annotations.
 * Provides fine-grained control over exception handling behavior.
 */
@Aspect
@Component
public class CustomExceptionAspect {

    private static final Logger logger = LoggerFactory.getLogger(CustomExceptionAspect.class);

    /**
     * Around advice for @HandleException annotation
     */
    @Around("@annotation(handleException)")
    public Object handleCustomException(ProceedingJoinPoint joinPoint, HandleException handleException) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String errorId = UUID.randomUUID().toString().substring(0, 8);
        
        MDC.put("errorId", errorId);
        MDC.put("component", "custom-exception-handler");
        MDC.put("className", className);
        MDC.put("methodName", methodName);

        try {
            Object result = joinPoint.proceed();
            return result;
        } catch (Exception e) {
            // Check if this exception should be handled
            if (shouldHandleException(e, handleException.exceptions())) {
                if (handleException.logException()) {
                    logger.error("Custom exception handling for {}.{}: {}", className, methodName, e.getMessage(), e);
                }
                
                // Create custom error response
                String message = handleException.message().isEmpty() ? e.getMessage() : handleException.message();
                ErrorResponse errorResponse = new ErrorResponse(
                    handleException.httpStatus(),
                    e.getClass().getSimpleName(),
                    message,
                    errorId,
                    System.currentTimeMillis(),
                    handleException.includeStackTrace() ? Arrays.toString(e.getStackTrace()) : null
                );
                
                return ResponseEntity.status(handleException.httpStatus()).body(errorResponse);
            } else {
                // Re-throw if not handled
                throw e;
            }
        } finally {
            MDC.clear();
        }
    }

    /**
     * Around advice for @RetryOnFailure annotation
     */
    @Around("@annotation(retryOnFailure)")
    public Object retryOnFailure(ProceedingJoinPoint joinPoint, RetryOnFailure retryOnFailure) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        
        int attempts = 0;
        long delay = retryOnFailure.delay();
        
        while (attempts < retryOnFailure.maxAttempts()) {
            try {
                Object result = joinPoint.proceed();
                if (attempts > 0) {
                    logger.info("Retry successful for {}.{} after {} attempts", className, methodName, attempts);
                }
                return result;
            } catch (Exception e) {
                attempts++;
                
                // Check if this exception should trigger retry
                if (!shouldRetry(e, retryOnFailure.retryOn(), retryOnFailure.noRetryOn())) {
                    throw e;
                }
                
                if (attempts >= retryOnFailure.maxAttempts()) {
                    logger.error("Retry failed for {}.{} after {} attempts: {}", className, methodName, attempts, e.getMessage());
                    throw e;
                }
                
                logger.warn("Retry attempt {} for {}.{}: {}", attempts, className, methodName, e.getMessage());
                
                // Calculate delay with exponential backoff
                if (retryOnFailure.exponentialBackoff()) {
                    delay = Math.min(delay * 2, retryOnFailure.maxDelay());
                }
                
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Retry interrupted", ie);
                }
            }
        }
        
        throw new RuntimeException("Retry exhausted");
    }

    /**
     * Around advice for @Timeout annotation
     */
    @Around("@annotation(timeout)")
    public Object handleTimeout(ProceedingJoinPoint joinPoint, Timeout timeout) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        
        // Create a timeout task
        TimeoutTask timeoutTask = new TimeoutTask(joinPoint, timeout);
        Thread timeoutThread = new Thread(timeoutTask);
        timeoutThread.start();
        
        try {
            // Wait for completion or timeout
            timeoutThread.join(timeout.value());
            
            if (timeoutThread.isAlive()) {
                // Timeout occurred
                if (timeout.logTimeout()) {
                    logger.warn("Timeout occurred for {}.{} after {}ms", className, methodName, timeout.value());
                }
                
                if (timeout.interrupt()) {
                    timeoutThread.interrupt();
                }
                
                throw new TimeoutException(timeout.message());
            }
            
            // Check for exceptions
            if (timeoutTask.getException() != null) {
                throw timeoutTask.getException();
            }
            
            return timeoutTask.getResult();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Timeout handling interrupted", e);
        }
    }

    /**
     * Check if exception should be handled
     */
    private boolean shouldHandleException(Exception e, Class<? extends Exception>[] exceptions) {
        if (exceptions.length == 0) {
            return true; // Handle all exceptions if none specified
        }
        
        for (Class<? extends Exception> exceptionClass : exceptions) {
            if (exceptionClass.isAssignableFrom(e.getClass())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if exception should trigger retry
     */
    private boolean shouldRetry(Exception e, Class<? extends Exception>[] retryOn, Class<? extends Exception>[] noRetryOn) {
        // Check no-retry list first
        for (Class<? extends Exception> exceptionClass : noRetryOn) {
            if (exceptionClass.isAssignableFrom(e.getClass())) {
                return false;
            }
        }
        
        // Check retry list
        for (Class<? extends Exception> exceptionClass : retryOn) {
            if (exceptionClass.isAssignableFrom(e.getClass())) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * Timeout task for executing methods with timeout
     */
    private static class TimeoutTask implements Runnable {
        private final ProceedingJoinPoint joinPoint;
        private final Timeout timeout;
        private Object result;
        private Exception exception;

        public TimeoutTask(ProceedingJoinPoint joinPoint, Timeout timeout) {
            this.joinPoint = joinPoint;
            this.timeout = timeout;
        }

        @Override
        public void run() {
            try {
                result = joinPoint.proceed();
            } catch (Throwable e) {
                exception = new RuntimeException(e);
            }
        }

        public Object getResult() {
            return result;
        }

        public Exception getException() {
            return exception;
        }
    }

    /**
     * Custom timeout exception
     */
    public static class TimeoutException extends RuntimeException {
        public TimeoutException(String message) {
            super(message);
        }
    }

    /**
     * Enhanced error response with stack trace
     */
    public static class ErrorResponse {
        private final int status;
        private final String error;
        private final String message;
        private final String errorId;
        private final long timestamp;
        private final String stackTrace;

        public ErrorResponse(int status, String error, String message, String errorId, long timestamp, String stackTrace) {
            this.status = status;
            this.error = error;
            this.message = message;
            this.errorId = errorId;
            this.timestamp = timestamp;
            this.stackTrace = stackTrace;
        }

        // Getters
        public int getStatus() { return status; }
        public String getError() { return error; }
        public String getMessage() { return message; }
        public String getErrorId() { return errorId; }
        public long getTimestamp() { return timestamp; }
        public String getStackTrace() { return stackTrace; }
    }
}
