package com.example.hello.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation for setting method execution timeouts.
 * Automatically cancels long-running operations to prevent resource exhaustion.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Timeout {
    
    /**
     * Timeout duration in milliseconds
     */
    long value() default 5000;
    
    /**
     * Custom timeout message
     */
    String message() default "Operation timed out";
    
    /**
     * Whether to log timeout events
     */
    boolean logTimeout() default true;
    
    /**
     * Whether to interrupt the thread on timeout
     */
    boolean interrupt() default true;
}
