package com.example.hello.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation for marking methods that should be logged with execution details.
 * This provides fine-grained control over what gets logged.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogExecution {
    
    /**
     * Log level for this method execution
     */
    String level() default "INFO";
    
    /**
     * Whether to log method arguments
     */
    boolean logArgs() default true;
    
    /**
     * Whether to log method return value
     */
    boolean logResult() default false;
    
    /**
     * Whether to log execution time
     */
    boolean logTime() default true;
    
    /**
     * Custom message to include in the log
     */
    String message() default "";
}
