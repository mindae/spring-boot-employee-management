package com.example.hello.controller;

import com.example.hello.annotation.CircuitBreaker;
import com.example.hello.annotation.HandleException;
import com.example.hello.annotation.RetryOnFailure;
import com.example.hello.annotation.Timeout;
import com.example.hello.model.Employee;
import com.example.hello.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Example controller demonstrating AOP-based exception handling.
 * Shows how to use custom annotations for different exception handling scenarios.
 */
@RestController
@RequestMapping("/api/exception-examples")
public class ExceptionHandlingExampleController {

    private final EmployeeService employeeService;

    public ExceptionHandlingExampleController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * Example with @HandleException annotation
     * Custom exception handling with specific HTTP status and message
     */
    @GetMapping("/employees/{id}")
    @HandleException(
        exceptions = {IllegalArgumentException.class, IllegalStateException.class},
        httpStatus = 400,
        message = "Invalid employee request",
        logException = true,
        includeStackTrace = false
    )
    public ResponseEntity<Employee> getEmployeeWithCustomHandling(@PathVariable Long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Employee ID must be positive");
        }
        return employeeService.getById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Example with @RetryOnFailure annotation
     * Automatically retries on transient failures
     */
    @GetMapping("/employees/retry/{id}")
    @RetryOnFailure(
        maxAttempts = 3,
        delay = 1000,
        exponentialBackoff = true,
        maxDelay = 5000,
        retryOn = {RuntimeException.class},
        noRetryOn = {IllegalArgumentException.class}
    )
    public ResponseEntity<Employee> getEmployeeWithRetry(@PathVariable Long id) {
        // Simulate transient failure
        if (Math.random() < 0.7) {
            throw new RuntimeException("Simulated transient failure");
        }
        return employeeService.getById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Example with @Timeout annotation
     * Automatically times out long-running operations
     */
    @GetMapping("/employees/slow/{id}")
    @Timeout(
        value = 2000, // 2 seconds
        message = "Employee lookup timed out",
        logTimeout = true,
        interrupt = true
    )
    public ResponseEntity<Employee> getEmployeeWithTimeout(@PathVariable Long id) {
        try {
            // Simulate slow operation
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Operation interrupted", e);
        }
        return employeeService.getById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Example with @CircuitBreaker annotation
     * Prevents cascading failures by temporarily stopping calls to failing services
     */
    @GetMapping("/employees/circuit-breaker/{id}")
    @CircuitBreaker(
        name = "employee-service",
        failureThreshold = 3,
        timeWindow = 60000, // 1 minute
        timeout = 30000, // 30 seconds
        fallbackMethod = "fallbackGetEmployee",
        logStateChanges = true
    )
    public ResponseEntity<Employee> getEmployeeWithCircuitBreaker(@PathVariable Long id) {
        // Simulate service failure
        if (Math.random() < 0.8) {
            throw new RuntimeException("Service temporarily unavailable");
        }
        return employeeService.getById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Fallback method for circuit breaker
     */
    public ResponseEntity<Employee> fallbackGetEmployee(Long id) {
        // Return cached or default response when circuit is open
        return ResponseEntity.ok(new Employee());
    }

    /**
     * Example with multiple annotations
     * Combines different exception handling strategies
     */
    @PostMapping("/employees/complex")
    @HandleException(
        exceptions = {IllegalArgumentException.class},
        httpStatus = 400,
        message = "Invalid employee data",
        logException = true
    )
    @RetryOnFailure(
        maxAttempts = 2,
        delay = 500,
        retryOn = {RuntimeException.class}
    )
    @Timeout(
        value = 5000,
        message = "Employee creation timed out"
    )
    public ResponseEntity<Employee> createEmployeeComplex(@RequestBody Employee employee) {
        // Validate employee data
        if (employee.getFirstName() == null || employee.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        
        // Simulate potential failure
        if (Math.random() < 0.3) {
            throw new RuntimeException("Simulated creation failure");
        }
        
        Employee saved = employeeService.create(employee);
        return ResponseEntity.ok(saved);
    }

    /**
     * Example with business logic validation
     * Shows how AOP handles business exceptions
     */
    @PutMapping("/employees/{id}/validate")
    public ResponseEntity<Employee> updateEmployeeWithValidation(@PathVariable Long id, @RequestBody Employee employee) {
        // Business logic validation
        if (employee.getSalary() != null && employee.getSalary().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new IllegalStateException("Salary cannot be negative");
        }
        
        if (employee.getEmail() != null && !employee.getEmail().contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        
        return employeeService.update(id, employee)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Example with external service call
     * Shows how to handle external service failures
     */
    @GetMapping("/employees/external/{id}")
    @RetryOnFailure(
        maxAttempts = 3,
        delay = 1000,
        exponentialBackoff = true,
        retryOn = {RuntimeException.class}
    )
    @Timeout(
        value = 10000,
        message = "External service call timed out"
    )
    public ResponseEntity<Employee> getEmployeeFromExternalService(@PathVariable Long id) {
        // Simulate external service call
        try {
            Thread.sleep(2000); // Simulate network delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("External service call interrupted", e);
        }
        
        // Simulate external service failure
        if (Math.random() < 0.6) {
            throw new RuntimeException("External service temporarily unavailable");
        }
        
        return employeeService.getById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
