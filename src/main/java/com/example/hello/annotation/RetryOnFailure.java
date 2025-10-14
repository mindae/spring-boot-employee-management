package com.example.hello.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation for marking methods that should be retried on failure.
 * Useful for transient failures like network timeouts or database connection issues.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RetryOnFailure {
    
    /**
     * Maximum number of retry attempts
     */
    int maxAttempts() default 3;
    
    /**
     * Delay between retries in milliseconds
     */
    long delay() default 1000;
    
    /**
     * Whether to use exponential backoff
     */
    boolean exponentialBackoff() default true;
    
    /**
     * Maximum delay between retries in milliseconds
     */
    long maxDelay() default 10000;
    
    /**
     * Types of exceptions that should trigger retry
     */
    Class<? extends Exception>[] retryOn() default {Exception.class};
    
    /**
     * Types of exceptions that should NOT trigger retry
     */
    Class<? extends Exception>[] noRetryOn() default {};
}
