package com.example.hello.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * AOP Aspect for security-related logging and monitoring.
 * Tracks authentication events, authorization checks, and security violations.
 */
@Aspect
@Component
public class SecurityAspect {

    private static final Logger logger = LoggerFactory.getLogger(SecurityAspect.class);

    /**
     * Pointcut for all controller methods that require authentication
     */
    @Pointcut("execution(* com.example.hello.controller.*.*(..))")
    public void controllerMethods() {}

    /**
     * Pointcut for security-related methods
     */
    @Pointcut("execution(* com.example.hello.config.*.*(..))")
    public void securityConfigMethods() {}

    /**
     * Before advice to log authentication context for controller methods
     */
    @Before("controllerMethods()")
    public void logAuthenticationContext(JoinPoint joinPoint) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            String authorities = authentication.getAuthorities().toString();
            
            MDC.put("username", username);
            MDC.put("authorities", authorities);
            
            logger.info("Authenticated request: {}.{} by user: {} with roles: {}", 
                className, methodName, username, authorities);
        } else {
            logger.warn("Unauthenticated request: {}.{}", className, methodName);
        }
    }

    /**
     * Before advice to log security configuration events
     */
    @Before("securityConfigMethods()")
    public void logSecurityConfiguration(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        
        logger.info("Security Configuration: {}.{}", className, methodName);
    }

    /**
     * Method to log authentication success
     */
    public void logAuthenticationSuccess(String username, String source) {
        MDC.put("username", username);
        logger.info("Authentication successful for user: {} from: {}", username, source);
    }

    /**
     * Method to log authentication failure
     */
    public void logAuthenticationFailure(String username, String reason, String source) {
        logger.warn("Authentication failed for user: {} from: {} - Reason: {}", username, source, reason);
    }

    /**
     * Method to log authorization success
     */
    public void logAuthorizationSuccess(String username, String resource, String action) {
        MDC.put("username", username);
        logger.info("Authorization successful: user: {} can {} on {}", username, action, resource);
    }

    /**
     * Method to log authorization failure
     */
    public void logAuthorizationFailure(String username, String resource, String action, String reason) {
        logger.warn("Authorization failed: user: {} cannot {} on {} - Reason: {}", 
            username, action, resource, reason);
    }

    /**
     * Method to log security violations
     */
    public void logSecurityViolation(String username, String violation, String details) {
        logger.error("Security violation: user: {} - {} - Details: {}", username, violation, details);
    }
}
