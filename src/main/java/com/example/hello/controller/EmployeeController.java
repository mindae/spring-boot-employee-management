package com.example.hello.controller;

import com.example.hello.model.Employee;
import com.example.hello.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
@Tag(name = "Employee Management", description = "APIs for managing employees")
@SecurityRequirement(name = "basicAuth")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

	@GetMapping
	@Operation(summary = "Get all employees", description = "Retrieve a list of all employees")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully retrieved list of employees"),
		@ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required")
	})
    public List<Employee> list() {
        return employeeService.listAll();
    }

	@GetMapping("/{id}")
	@Operation(summary = "Get employee by ID", description = "Retrieve a specific employee by their ID")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully retrieved employee"),
		@ApiResponse(responseCode = "404", description = "Employee not found"),
		@ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required")
	})
    public ResponseEntity<Employee> get(
		@Parameter(description = "Employee ID", required = true, example = "1")
		@PathVariable Long id) {
        return employeeService.getById(id)
			.map(ResponseEntity::ok)
			.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping
	@Operation(summary = "Create new employee", description = "Create a new employee record")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "Employee created successfully"),
		@ApiResponse(responseCode = "400", description = "Invalid employee data"),
		@ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required")
	})
    public ResponseEntity<Employee> create(@RequestBody Employee employee) {
        Employee saved = employeeService.create(employee);
		return ResponseEntity.created(URI.create("/api/employees/" + saved.getId())).body(saved);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Update employee", description = "Update an existing employee by ID")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Employee updated successfully"),
		@ApiResponse(responseCode = "404", description = "Employee not found"),
		@ApiResponse(responseCode = "400", description = "Invalid employee data"),
		@ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required")
	})
    public ResponseEntity<Employee> update(
		@Parameter(description = "Employee ID", required = true, example = "1")
		@PathVariable Long id, 
		@RequestBody Employee update) {
        return employeeService.update(id, update)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete employee", description = "Delete an employee by ID")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "204", description = "Employee deleted successfully"),
		@ApiResponse(responseCode = "404", description = "Employee not found"),
		@ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required")
	})
    public ResponseEntity<Void> delete(
		@Parameter(description = "Employee ID", required = true, example = "1")
		@PathVariable Long id) {
        boolean deleted = employeeService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}


