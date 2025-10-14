package com.example.hello.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation for implementing circuit breaker pattern.
 * Prevents cascading failures by temporarily stopping calls to failing services.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CircuitBreaker {
    
    /**
     * Name of the circuit breaker
     */
    String name() default "";
    
    /**
     * Failure threshold before opening the circuit
     */
    int failureThreshold() default 5;
    
    /**
     * Time window for failure counting in milliseconds
     */
    long timeWindow() default 60000; // 1 minute
    
    /**
     * Time to wait before attempting to close the circuit in milliseconds
     */
    long timeout() default 30000; // 30 seconds
    
    /**
     * Fallback method to call when circuit is open
     */
    String fallbackMethod() default "";
    
    /**
     * Whether to log circuit breaker state changes
     */
    boolean logStateChanges() default true;
}
