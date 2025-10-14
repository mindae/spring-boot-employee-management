package com.example.hello.aspect;

import com.example.hello.annotation.AuditLog;
import com.example.hello.annotation.LogExecution;
import com.example.hello.annotation.LogPerformance;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

/**
 * AOP Aspect for handling custom logging annotations.
 * Provides fine-grained control over logging behavior using annotations.
 */
@Aspect
@Component
public class CustomLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(CustomLoggingAspect.class);
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * Around advice for @LogExecution annotation
     */
    @Around("@annotation(logExecution)")
    public Object logExecution(ProceedingJoinPoint joinPoint, LogExecution logExecution) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        
        // Log method entry with arguments if requested
        if (logExecution.logArgs()) {
            logger.info("Executing: {}.{} with args: {}", className, methodName, 
                Arrays.toString(joinPoint.getArgs()));
        } else {
            logger.info("Executing: {}.{}", className, methodName);
        }
        
        // Add custom message if provided
        if (!logExecution.message().isEmpty()) {
            logger.info("Custom message: {}", logExecution.message());
        }
        
        long startTime = System.currentTimeMillis();
        Object result = null;
        
        try {
            result = joinPoint.proceed();
            return result;
        } catch (Exception e) {
            logger.error("Error executing: {}.{} - {}", className, methodName, e.getMessage(), e);
            throw e;
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            
            // Log execution time if requested
            if (logExecution.logTime()) {
                logger.info("Completed: {}.{} in {}ms", className, methodName, duration);
            }
            
            // Log result if requested
            if (logExecution.logResult() && result != null) {
                logger.info("Result: {}.{} returned: {}", className, methodName, result);
            }
        }
    }

    /**
     * Around advice for @LogPerformance annotation
     */
    @Around("@annotation(logPerformance)")
    public Object logPerformance(ProceedingJoinPoint joinPoint, LogPerformance logPerformance) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String operationName = logPerformance.operationName().isEmpty() ? methodName : logPerformance.operationName();
        
        long startTime = System.currentTimeMillis();
        
        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;
            
            // Log performance metrics
            if (logPerformance.collectMetrics()) {
                logger.info("Performance: {} completed in {}ms", operationName, duration);
            }
            
            // Alert on slow operations
            if (duration > logPerformance.slowThreshold()) {
                logger.warn("Slow operation: {} took {}ms (threshold: {}ms)", 
                    operationName, duration, logPerformance.slowThreshold());
            }
            
            return result;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            logger.error("Performance error: {} failed after {}ms - {}", 
                operationName, duration, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Around advice for @AuditLog annotation
     */
    @Around("@annotation(auditLog)")
    public Object auditLog(ProceedingJoinPoint joinPoint, AuditLog auditLog) throws Throwable {
        
        // Get user context if available
        String username = "SYSTEM";
        if (auditLog.logUser()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                username = authentication.getName();
            }
        }
        
        // Get timestamp if requested
        String timestamp = "";
        if (auditLog.logTimestamp()) {
            timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
        }
        
        // Set audit context
        MDC.put("auditAction", auditLog.action());
        MDC.put("auditResource", auditLog.resource());
        MDC.put("auditUser", username);
        MDC.put("auditTimestamp", timestamp);
        
        // Log audit entry
        logger.info("AUDIT: {} performed {} on {} by {} at {}", 
            auditLog.action(), auditLog.resource(), username, timestamp);
        
        // Add custom message if provided
        if (!auditLog.message().isEmpty()) {
            logger.info("AUDIT MESSAGE: {}", auditLog.message());
        }
        
        try {
            Object result = joinPoint.proceed();
            
            // Log successful audit
            logger.info("AUDIT SUCCESS: {} completed successfully", auditLog.action());
            
            return result;
        } catch (Exception e) {
            // Log failed audit
            logger.error("AUDIT FAILURE: {} failed - {}", auditLog.action(), e.getMessage(), e);
            throw e;
        } finally {
            // Clear audit context
            MDC.remove("auditAction");
            MDC.remove("auditResource");
            MDC.remove("auditUser");
            MDC.remove("auditTimestamp");
        }
    }
}
