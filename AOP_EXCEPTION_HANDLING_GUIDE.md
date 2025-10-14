# AOP-Based Exception Handling Implementation Guide

## Overview
This application now uses **Aspect-Oriented Programming (AOP)** for comprehensive exception handling across the application. This approach eliminates the need for manual try-catch blocks and provides centralized, consistent error handling.

## Benefits of AOP-Based Exception Handling

### ✅ **Clean Code**
- No manual try-catch blocks cluttering business logic
- Separation of concerns - exception handling is handled by aspects
- Easier to maintain and modify exception handling behavior

### ✅ **Consistent Error Handling**
- All exceptions are handled consistently across the application
- Standardized error responses and logging
- No risk of forgetting to handle exceptions in new methods

### ✅ **Advanced Patterns**
- **Retry Logic**: Automatic retry for transient failures
- **Circuit Breaker**: Prevents cascading failures
- **Timeout Handling**: Automatic cancellation of long-running operations
- **Custom Error Responses**: Fine-grained control over error handling

### ✅ **Production Ready**
- Structured error logging with correlation IDs
- Performance monitoring for exception patterns
- Security-aware error handling
- Compliance-ready audit trails

## AOP Components

### 1. **GlobalExceptionAspect** - Automatic Exception Handling
```java
@Aspect
@Component
public class GlobalExceptionAspect {
    // Automatically handles exceptions in controllers, services, and repositories
    // Provides standardized error responses
    // Logs exceptions with context and correlation IDs
}
```

**Features:**
- **Controller Methods**: HTTP error responses with appropriate status codes
- **Service Methods**: Business exception handling with proper error propagation
- **Repository Methods**: Data access exception handling
- **Error Correlation**: Unique error IDs for tracking
- **Structured Logging**: Context-rich error logging

### 2. **CustomExceptionAspect** - Annotation-Based Exception Handling
```java
@Aspect
@Component
public class CustomExceptionAspect {
    // Handles custom exception annotations
    // Provides fine-grained control over exception handling
}
```

**Features:**
- **@HandleException**: Custom exception handling with specific HTTP status codes
- **@RetryOnFailure**: Automatic retry with exponential backoff
- **@Timeout**: Automatic timeout handling for long-running operations
- **@CircuitBreaker**: Circuit breaker pattern implementation

### 3. **ExceptionUtils** - Exception Handling Utilities
```java
public class ExceptionUtils {
    // Utility methods for structured exception handling
    // Context-rich error logging
    // Exception classification and severity assessment
}
```

**Features:**
- **Structured Logging**: Context-rich error logging with correlation IDs
- **Exception Classification**: Automatic severity assessment
- **Error Response Creation**: Standardized error response generation
- **Context Management**: MDC-based error context tracking

## Custom Annotations

### @HandleException
```java
@HandleException(
    exceptions = {IllegalArgumentException.class, IllegalStateException.class},
    httpStatus = 400,
    message = "Custom error message",
    logException = true,
    includeStackTrace = false
)
public void someMethod() { ... }
```

**Features:**
- **Exception Filtering**: Handle specific exception types
- **Custom HTTP Status**: Set appropriate HTTP status codes
- **Custom Messages**: Override default error messages
- **Logging Control**: Control exception logging behavior
- **Stack Trace Control**: Include/exclude stack traces in responses

### @RetryOnFailure
```java
@RetryOnFailure(
    maxAttempts = 3,
    delay = 1000,
    exponentialBackoff = true,
    maxDelay = 10000,
    retryOn = {RuntimeException.class},
    noRetryOn = {IllegalArgumentException.class}
)
public void someMethod() { ... }
```

**Features:**
- **Retry Logic**: Automatic retry for transient failures
- **Exponential Backoff**: Increasing delays between retries
- **Exception Filtering**: Retry on specific exception types
- **Max Attempts**: Configurable retry limits
- **Delay Control**: Customizable retry delays

### @Timeout
```java
@Timeout(
    value = 5000, // 5 seconds
    message = "Operation timed out",
    logTimeout = true,
    interrupt = true
)
public void someMethod() { ... }
```

**Features:**
- **Timeout Control**: Set maximum execution time
- **Automatic Cancellation**: Cancel long-running operations
- **Custom Messages**: Override timeout messages
- **Logging**: Log timeout events
- **Thread Interruption**: Control thread interruption behavior

### @CircuitBreaker
```java
@CircuitBreaker(
    name = "service-name",
    failureThreshold = 5,
    timeWindow = 60000,
    timeout = 30000,
    fallbackMethod = "fallbackMethod",
    logStateChanges = true
)
public void someMethod() { ... }
```

**Features:**
- **Failure Threshold**: Number of failures before opening circuit
- **Time Window**: Time window for failure counting
- **Circuit Timeout**: Time to wait before attempting to close circuit
- **Fallback Methods**: Alternative methods when circuit is open
- **State Logging**: Log circuit breaker state changes

## Usage Examples

### Basic Controller (Automatic Exception Handling)
```java
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    
    // AOP automatically handles exceptions
    @GetMapping("/{id}")
    public ResponseEntity<Employee> get(@PathVariable Long id) {
        return employeeService.getById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    // AOP automatically handles exceptions with retry logic
    @PostMapping
    public ResponseEntity<Employee> create(@RequestBody Employee employee) {
        Employee saved = employeeService.create(employee);
        return ResponseEntity.ok(saved);
    }
}
```

### Advanced Controller (Custom Annotations)
```java
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    
    // Custom exception handling
    @GetMapping("/{id}")
    @HandleException(
        exceptions = {IllegalArgumentException.class},
        httpStatus = 400,
        message = "Invalid employee ID"
    )
    public ResponseEntity<Employee> get(@PathVariable Long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Employee ID must be positive");
        }
        return employeeService.getById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    // Retry logic for transient failures
    @PostMapping
    @RetryOnFailure(
        maxAttempts = 3,
        delay = 1000,
        exponentialBackoff = true
    )
    public ResponseEntity<Employee> create(@RequestBody Employee employee) {
        Employee saved = employeeService.create(employee);
        return ResponseEntity.ok(saved);
    }
    
    // Timeout handling for long-running operations
    @GetMapping("/slow/{id}")
    @Timeout(value = 5000, message = "Employee lookup timed out")
    public ResponseEntity<Employee> getSlow(@PathVariable Long id) {
        // Simulate slow operation
        Thread.sleep(3000);
        return employeeService.getById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    // Circuit breaker for external services
    @GetMapping("/external/{id}")
    @CircuitBreaker(
        name = "employee-service",
        failureThreshold = 3,
        fallbackMethod = "fallbackGetEmployee"
    )
    public ResponseEntity<Employee> getFromExternalService(@PathVariable Long id) {
        // External service call
        return employeeService.getById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    public ResponseEntity<Employee> fallbackGetEmployee(Long id) {
        // Fallback response when circuit is open
        return ResponseEntity.ok(new Employee());
    }
}
```

## Error Response Structure

### Standard Error Response
```json
{
    "status": 400,
    "error": "IllegalArgumentException",
    "message": "Invalid employee ID",
    "errorId": "a1b2c3d4",
    "timestamp": 1642248000000
}
```

### Enhanced Error Response (with context)
```json
{
    "status": 500,
    "error": "ServiceException",
    "message": "Service temporarily unavailable",
    "errorId": "e5f6g7h8",
    "timestamp": 1642248000000,
    "context": "external-service",
    "entityId": "123"
}
```

## Log Output Examples

### Automatic Exception Handling
```
2024-01-15 10:30:45.123 [http-nio-8080-exec-1] ERROR [GlobalExceptionAspect] - Validation error in EmployeeController.get: Invalid employee ID - Error ID: a1b2c3d4
2024-01-15 10:30:45.156 [http-nio-8080-exec-1] ERROR [GlobalExceptionAspect] - Business logic error in EmployeeService.create: Salary cannot be negative - Error ID: e5f6g7h8
```

### Retry Logic
```
2024-01-15 10:30:45.200 [http-nio-8080-exec-1] WARN  [CustomExceptionAspect] - Retry attempt 1 for EmployeeService.create: Service temporarily unavailable
2024-01-15 10:30:45.300 [http-nio-8080-exec-1] WARN  [CustomExceptionAspect] - Retry attempt 2 for EmployeeService.create: Service temporarily unavailable
2024-01-15 10:30:45.400 [http-nio-8080-exec-1] INFO  [CustomExceptionAspect] - Retry successful for EmployeeService.create after 3 attempts
```

### Circuit Breaker
```
2024-01-15 10:30:45.500 [http-nio-8080-exec-1] INFO  [CustomExceptionAspect] - Circuit breaker 'employee-service' opened after 3 failures
2024-01-15 10:30:45.600 [http-nio-8080-exec-1] WARN  [CustomExceptionAspect] - Circuit breaker 'employee-service' is open, using fallback method
```

### Timeout Handling
```
2024-01-15 10:30:45.700 [http-nio-8080-exec-1] WARN  [CustomExceptionAspect] - Timeout occurred for EmployeeService.getSlow after 5000ms
```

## Exception Handling Patterns

### 1. **Validation Errors**
```java
@HandleException(
    exceptions = {IllegalArgumentException.class},
    httpStatus = 400,
    message = "Validation failed"
)
public void validateData(String data) {
    if (data == null || data.trim().isEmpty()) {
        throw new IllegalArgumentException("Data cannot be empty");
    }
}
```

### 2. **Business Logic Errors**
```java
@HandleException(
    exceptions = {IllegalStateException.class},
    httpStatus = 409,
    message = "Business rule violation"
)
public void processOrder(Order order) {
    if (order.getStatus() == OrderStatus.CANCELLED) {
        throw new IllegalStateException("Cannot process cancelled order");
    }
}
```

### 3. **Transient Failures**
```java
@RetryOnFailure(
    maxAttempts = 3,
    delay = 1000,
    exponentialBackoff = true,
    retryOn = {RuntimeException.class}
)
public void callExternalService() {
    // External service call that might fail temporarily
}
```

### 4. **Long-Running Operations**
```java
@Timeout(
    value = 10000,
    message = "Operation timed out",
    interrupt = true
)
public void processLargeDataset() {
    // Long-running data processing
}
```

### 5. **External Service Calls**
```java
@CircuitBreaker(
    name = "external-service",
    failureThreshold = 5,
    timeWindow = 60000,
    fallbackMethod = "fallbackMethod"
)
public void callExternalService() {
    // External service call
}
```

## Best Practices

### 1. **Exception Classification**
- Use appropriate exception types for different scenarios
- Implement custom exceptions for business logic
- Classify exceptions by severity and retryability

### 2. **Error Context**
- Always include correlation IDs for error tracking
- Log relevant context (user ID, entity ID, operation)
- Use structured logging for better analysis

### 3. **Retry Logic**
- Only retry on transient failures
- Use exponential backoff for retry delays
- Set reasonable retry limits

### 4. **Circuit Breaker**
- Use circuit breakers for external service calls
- Implement meaningful fallback methods
- Monitor circuit breaker state changes

### 5. **Timeout Handling**
- Set appropriate timeouts for different operations
- Use timeouts to prevent resource exhaustion
- Log timeout events for monitoring

### 6. **Security Considerations**
- Don't expose sensitive information in error messages
- Log security-related exceptions appropriately
- Use appropriate HTTP status codes

## Migration from Manual Exception Handling

### Before (Manual Exception Handling)
```java
@RestController
public class EmployeeController {
    
    @GetMapping("/{id}")
    public ResponseEntity<Employee> get(@PathVariable Long id) {
        try {
            Optional<Employee> employee = employeeService.getById(id);
            if (employee.isPresent()) {
                return ResponseEntity.ok(employee.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            logger.error("Invalid employee ID: {}", id, e);
            return ResponseEntity.badRequest().body("Invalid employee ID");
        } catch (Exception e) {
            logger.error("Error retrieving employee: {}", id, e);
            return ResponseEntity.status(500).body("Internal server error");
        }
    }
}
```

### After (AOP-Based Exception Handling)
```java
@RestController
public class EmployeeController {
    
    // No manual exception handling needed!
    @GetMapping("/{id}")
    public ResponseEntity<Employee> get(@PathVariable Long id) {
        return employeeService.getById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
```

## Production Benefits

### 1. **Automatic Coverage**
- All methods have exception handling without manual intervention
- No risk of missing exception handling on new methods
- Consistent error handling across the entire application

### 2. **Advanced Patterns**
- Automatic retry for transient failures
- Circuit breaker for external services
- Timeout handling for long-running operations
- Custom error responses for different scenarios

### 3. **Monitoring and Observability**
- Structured error logging with correlation IDs
- Exception pattern analysis
- Performance impact assessment
- Security event tracking

### 4. **Maintainability**
- Clean business logic without exception handling pollution
- Centralized exception handling configuration
- Easy to modify exception handling behavior
- Consistent error response formats

This AOP-based approach provides enterprise-grade exception handling while keeping your business logic clean and maintainable. The exception handling is now a true cross-cutting concern that's handled automatically by the framework! 🚀
