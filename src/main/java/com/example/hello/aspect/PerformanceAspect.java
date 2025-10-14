package com.example.hello.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * AOP Aspect for performance monitoring and metrics collection.
 * Tracks method execution times, slow operations, and performance trends.
 */
@Aspect
@Component
public class PerformanceAspect {

    private static final Logger logger = LoggerFactory.getLogger(PerformanceAspect.class);
    
    // Performance metrics storage
    private final ConcurrentHashMap<String, AtomicLong> methodCallCounts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, AtomicLong> methodTotalTimes = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> methodMaxTimes = new ConcurrentHashMap<>();

    /**
     * Pointcut for all public methods in the application
     */
    @Pointcut("execution(public * com.example.hello..*(..))")
    public void allPublicMethods() {}

    /**
     * Pointcut for controller methods
     */
    @Pointcut("execution(* com.example.hello.controller.*.*(..))")
    public void controllerMethods() {}

    /**
     * Pointcut for service methods
     */
    @Pointcut("execution(* com.example.hello.service.*.*(..))")
    public void serviceMethods() {}

    /**
     * Pointcut for repository methods
     */
    @Pointcut("execution(* com.example.hello.repository.*.*(..))")
    public void repositoryMethods() {}

    /**
     * Around advice for performance monitoring
     */
    @Around("allPublicMethods()")
    public Object monitorPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodSignature = joinPoint.getSignature().toShortString();
        String methodKey = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        
        long startTime = System.currentTimeMillis();
        
        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;
            
            // Update performance metrics
            updateMetrics(methodKey, duration);
            
            // Log slow operations
            if (duration > getSlowOperationThreshold(joinPoint)) {
                logger.warn("Slow operation detected: {} took {}ms", methodSignature, duration);
            }
            
            return result;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            logger.error("Operation failed: {} took {}ms before failing", methodSignature, duration);
            throw e;
        }
    }

    /**
     * Around advice specifically for HTTP requests
     */
    @Around("controllerMethods()")
    public Object monitorHttpRequests(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        
        MDC.put("component", "http-request");
        MDC.put("className", className);
        MDC.put("methodName", methodName);
        
        long startTime = System.currentTimeMillis();
        
        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;
            
            // Log HTTP request performance
            logger.info("HTTP Request: {}.{} completed in {}ms", className, methodName, duration);
            
            // Alert on slow HTTP requests
            if (duration > 2000) {
                logger.warn("Slow HTTP request: {}.{} took {}ms", className, methodName, duration);
            }
            
            return result;
        } finally {
            MDC.remove("component");
            MDC.remove("className");
            MDC.remove("methodName");
        }
    }

    /**
     * Around advice for database operations
     */
    @Around("repositoryMethods()")
    public Object monitorDatabaseOperations(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        
        long startTime = System.currentTimeMillis();
        
        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;
            
            // Log database operation performance
            logger.debug("Database operation: {}.{} completed in {}ms", className, methodName, duration);
            
            // Alert on slow database operations
            if (duration > 1000) {
                logger.warn("Slow database operation: {}.{} took {}ms", className, methodName, duration);
            }
            
            return result;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            logger.error("Database operation failed: {}.{} failed after {}ms", className, methodName, duration);
            throw e;
        }
    }

    /**
     * Update performance metrics for a method
     */
    private void updateMetrics(String methodKey, long duration) {
        // Update call count
        methodCallCounts.computeIfAbsent(methodKey, k -> new AtomicLong(0)).incrementAndGet();
        
        // Update total time
        methodTotalTimes.computeIfAbsent(methodKey, k -> new AtomicLong(0)).addAndGet(duration);
        
        // Update max time
        methodMaxTimes.merge(methodKey, duration, Math::max);
    }

    /**
     * Get slow operation threshold based on method type
     */
    private long getSlowOperationThreshold(ProceedingJoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        
        if (className.contains("Controller")) {
            return 2000; // 2 seconds for HTTP requests
        } else if (className.contains("Service")) {
            return 1000; // 1 second for business operations
        } else if (className.contains("Repository")) {
            return 500;  // 500ms for database operations
        }
        
        return 1000; // Default threshold
    }

    /**
     * Get performance statistics for a method
     */
    public PerformanceStats getPerformanceStats(String methodKey) {
        AtomicLong callCount = methodCallCounts.get(methodKey);
        AtomicLong totalTime = methodTotalTimes.get(methodKey);
        Long maxTime = methodMaxTimes.get(methodKey);
        
        if (callCount == null || callCount.get() == 0) {
            return new PerformanceStats(0, 0, 0, 0);
        }
        
        long calls = callCount.get();
        long total = totalTime.get();
        long avgTime = total / calls;
        long max = maxTime != null ? maxTime : 0;
        
        return new PerformanceStats(calls, total, avgTime, max);
    }

    /**
     * Performance statistics data class
     */
    public static class PerformanceStats {
        private final long callCount;
        private final long totalTime;
        private final long averageTime;
        private final long maxTime;

        public PerformanceStats(long callCount, long totalTime, long averageTime, long maxTime) {
            this.callCount = callCount;
            this.totalTime = totalTime;
            this.averageTime = averageTime;
            this.maxTime = maxTime;
        }

        // Getters
        public long getCallCount() { return callCount; }
        public long getTotalTime() { return totalTime; }
        public long getAverageTime() { return averageTime; }
        public long getMaxTime() { return maxTime; }

        @Override
        public String toString() {
            return String.format("PerformanceStats{calls=%d, total=%dms, avg=%dms, max=%dms}", 
                callCount, totalTime, averageTime, maxTime);
        }
    }
}
