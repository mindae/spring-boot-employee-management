package com.example.hello.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation for marking methods that require specific exception handling.
 * Provides fine-grained control over exception handling behavior.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HandleException {
    
    /**
     * The type of exception to handle
     */
    Class<? extends Exception>[] exceptions() default {};
    
    /**
     * HTTP status code to return for this exception
     */
    int httpStatus() default 500;
    
    /**
     * Custom error message to return
     */
    String message() default "";
    
    /**
     * Whether to log the exception
     */
    boolean logException() default true;
    
    /**
     * Whether to include stack trace in response (for development)
     */
    boolean includeStackTrace() default false;
    
    /**
     * Whether to retry the operation on failure
     */
    boolean retry() default false;
    
    /**
     * Number of retry attempts
     */
    int maxRetries() default 3;
}
