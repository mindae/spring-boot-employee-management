package com.example.hello.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation for marking methods that require performance monitoring.
 * These methods will be tracked for execution time and performance metrics.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogPerformance {
    
    /**
     * Threshold in milliseconds for considering this operation as slow
     */
    long slowThreshold() default 1000;
    
    /**
     * Whether to collect detailed performance metrics
     */
    boolean collectMetrics() default true;
    
    /**
     * Custom operation name for metrics collection
     */
    String operationName() default "";
}
