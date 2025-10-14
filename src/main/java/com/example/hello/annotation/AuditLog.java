package com.example.hello.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation for marking methods that require audit logging.
 * These methods will be logged for compliance and security auditing purposes.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuditLog {
    
    /**
     * The action being performed (e.g., "CREATE_EMPLOYEE", "DELETE_USER")
     */
    String action();
    
    /**
     * The resource being affected (e.g., "EMPLOYEE", "USER", "ORDER")
     */
    String resource();
    
    /**
     * Whether to log the user who performed the action
     */
    boolean logUser() default true;
    
    /**
     * Whether to log the timestamp of the action
     */
    boolean logTimestamp() default true;
    
    /**
     * Custom message for the audit log
     */
    String message() default "";
}
