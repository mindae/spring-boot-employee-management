package com.example.hello.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.UUID;

/**
 * AOP Aspect for automatic logging of method execution across the application.
 * This eliminates the need for manual logging statements in business logic.
 */
@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    /**
     * Pointcut for all controller methods
     */
    @Pointcut("execution(* com.example.hello.controller.*.*(..))")
    public void controllerMethods() {}

    /**
     * Pointcut for all service methods
     */
    @Pointcut("execution(* com.example.hello.service.*.*(..))")
    public void serviceMethods() {}

    /**
     * Pointcut for all repository methods
     */
    @Pointcut("execution(* com.example.hello.repository.*.*(..))")
    public void repositoryMethods() {}

    /**
     * Pointcut for all public methods in the application
     */
    @Pointcut("execution(public * com.example.hello..*(..))")
    public void allPublicMethods() {}

    /**
     * Around advice for controller methods - logs HTTP requests
     */
    @Around("controllerMethods()")
    public Object logControllerMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String correlationId = UUID.randomUUID().toString().substring(0, 8);
        
        MDC.put("correlationId", correlationId);
        MDC.put("component", "controller");
        MDC.put("className", className);
        MDC.put("methodName", methodName);

        logger.info("HTTP Request: {}.{} with args: {}", className, methodName, 
            Arrays.toString(joinPoint.getArgs()));

        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;
            
            logger.info("HTTP Response: {}.{} completed in {}ms", className, methodName, duration);
            return result;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            logger.error("HTTP Error: {}.{} failed in {}ms with error: {}", 
                className, methodName, duration, e.getMessage(), e);
            throw e;
        } finally {
            MDC.clear();
        }
    }

    /**
     * Around advice for service methods - logs business operations
     */
    @Around("serviceMethods()")
    public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        
        MDC.put("component", "service");
        MDC.put("className", className);
        MDC.put("methodName", methodName);

        logger.info("Business Operation: {}.{} with args: {}", className, methodName, 
            Arrays.toString(joinPoint.getArgs()));

        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;
            
            if (duration > 1000) {
                logger.warn("Slow business operation: {}.{} took {}ms", className, methodName, duration);
            } else {
                logger.info("Business Operation: {}.{} completed in {}ms", className, methodName, duration);
            }
            return result;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            logger.error("Business Operation Error: {}.{} failed in {}ms with error: {}", 
                className, methodName, duration, e.getMessage(), e);
            throw e;
        } finally {
            MDC.remove("component");
            MDC.remove("className");
            MDC.remove("methodName");
        }
    }

    /**
     * Around advice for repository methods - logs database operations
     */
    @Around("repositoryMethods()")
    public Object logRepositoryMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        
        MDC.put("component", "repository");
        MDC.put("className", className);
        MDC.put("methodName", methodName);

        logger.debug("Database Operation: {}.{} with args: {}", className, methodName, 
            Arrays.toString(joinPoint.getArgs()));

        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;
            
            if (duration > 500) {
                logger.warn("Slow database operation: {}.{} took {}ms", className, methodName, duration);
            } else {
                logger.debug("Database Operation: {}.{} completed in {}ms", className, methodName, duration);
            }
            return result;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            logger.error("Database Operation Error: {}.{} failed in {}ms with error: {}", 
                className, methodName, duration, e.getMessage(), e);
            throw e;
        } finally {
            MDC.remove("component");
            MDC.remove("className");
            MDC.remove("methodName");
        }
    }
}
