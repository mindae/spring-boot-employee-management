package com.example.hello.controller;

import com.example.hello.annotation.AuditLog;
import com.example.hello.annotation.LogExecution;
import com.example.hello.annotation.LogPerformance;
import com.example.hello.model.Employee;
import com.example.hello.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Example controller demonstrating how to use AOP-based logging with custom annotations.
 * This shows the clean approach without manual logging statements.
 */
@RestController
@RequestMapping("/api/example")
public class ExampleController {

    private final EmployeeService employeeService;

    public ExampleController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * Example method with @LogExecution annotation
     * This will automatically log method execution with arguments and timing
     */
    @GetMapping("/employees")
    @LogExecution(level = "INFO", logArgs = true, logTime = true, message = "Retrieving all employees")
    public List<Employee> getAllEmployees() {
        return employeeService.listAll();
    }

    /**
     * Example method with @LogPerformance annotation
     * This will monitor performance and alert on slow operations
     */
    @GetMapping("/employees/{id}")
    @LogPerformance(slowThreshold = 500, collectMetrics = true, operationName = "GET_EMPLOYEE")
    public ResponseEntity<Employee> getEmployee(@PathVariable Long id) {
        return employeeService.getById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Example method with @AuditLog annotation
     * This will create audit logs for compliance and security
     */
    @PostMapping("/employees")
    @AuditLog(
        action = "CREATE_EMPLOYEE", 
        resource = "EMPLOYEE", 
        logUser = true, 
        logTimestamp = true,
        message = "New employee created"
    )
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        Employee saved = employeeService.create(employee);
        return ResponseEntity.ok(saved);
    }

    /**
     * Example method with multiple annotations
     * This demonstrates combining different logging concerns
     */
    @PutMapping("/employees/{id}")
    @LogExecution(level = "INFO", logArgs = true, logTime = true)
    @LogPerformance(slowThreshold = 1000, collectMetrics = true)
    @AuditLog(action = "UPDATE_EMPLOYEE", resource = "EMPLOYEE", logUser = true)
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        return employeeService.update(id, employee)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Example method with @AuditLog for deletion
     * This will create audit logs for the deletion operation
     */
    @DeleteMapping("/employees/{id}")
    @AuditLog(
        action = "DELETE_EMPLOYEE", 
        resource = "EMPLOYEE", 
        logUser = true, 
        logTimestamp = true,
        message = "Employee deleted"
    )
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        boolean deleted = employeeService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
