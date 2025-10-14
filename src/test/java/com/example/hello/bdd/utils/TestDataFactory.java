package com.example.hello.bdd.utils;

import com.example.hello.bdd.builders.EmployeeTestDataBuilder;
import com.example.hello.model.Employee;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Factory for creating test data.
 * Provides methods for generating various test scenarios.
 */
public class TestDataFactory {

    private static final Random random = new Random();

    /**
     * Create a valid employee for testing
     */
    public static Employee createValidEmployee() {
        return EmployeeTestDataBuilder.anEmployee().build();
    }

    /**
     * Create an employee with specific name
     */
    public static Employee createEmployeeWithName(String firstName, String lastName) {
        return EmployeeTestDataBuilder.anEmployee()
                .withName(firstName, lastName)
                .build();
    }

    /**
     * Create an employee with invalid data
     */
    public static Employee createInvalidEmployee() {
        return EmployeeTestDataBuilder.anEmployee()
                .withInvalidData()
                .build();
    }

    /**
     * Create a developer employee
     */
    public static Employee createDeveloper() {
        return EmployeeTestDataBuilder.anEmployee()
                .asDeveloper()
                .build();
    }

    /**
     * Create a manager employee
     */
    public static Employee createManager() {
        return EmployeeTestDataBuilder.anEmployee()
                .asManager()
                .build();
    }

    /**
     * Create a sales representative
     */
    public static Employee createSalesRep() {
        return EmployeeTestDataBuilder.anEmployee()
                .asSalesRep()
                .build();
    }

    /**
     * Create multiple employees for testing
     */
    public static List<Employee> createMultipleEmployees(int count) {
        List<Employee> employees = new ArrayList<>();
        String[] firstNames = {"John", "Jane", "Bob", "Alice", "Charlie", "Diana", "Eve", "Frank"};
        String[] lastNames = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis"};

        for (int i = 0; i < count; i++) {
            String firstName = firstNames[random.nextInt(firstNames.length)];
            String lastName = lastNames[random.nextInt(lastNames.length)];
            String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@example.com";

            Employee employee = EmployeeTestDataBuilder.anEmployee()
                    .withName(firstName, lastName)
                    .withEmail(email)
                    .build();
            employees.add(employee);
        }

        return employees;
    }

    /**
     * Create employees with different roles
     */
    public static List<Employee> createEmployeesWithDifferentRoles() {
        List<Employee> employees = new ArrayList<>();
        employees.add(createDeveloper());
        employees.add(createManager());
        employees.add(createSalesRep());
        return employees;
    }

    /**
     * Create an employee with specific ID
     */
    public static Employee createEmployeeWithId(Long id) {
        return EmployeeTestDataBuilder.anEmployee()
                .withId(id)
                .build();
    }

    /**
     * Create an employee with high salary
     */
    public static Employee createHighSalaryEmployee() {
        return EmployeeTestDataBuilder.anEmployee()
                .withSalary(java.math.BigDecimal.valueOf(150000))
                .build();
    }

    /**
     * Create an employee with low salary
     */
    public static Employee createLowSalaryEmployee() {
        return EmployeeTestDataBuilder.anEmployee()
                .withSalary(java.math.BigDecimal.valueOf(30000))
                .build();
    }

    /**
     * Create an employee for performance testing
     */
    public static Employee createEmployeeForPerformanceTest() {
        return EmployeeTestDataBuilder.anEmployee()
                .withName("Performance", "Test")
                .withEmail("performance.test@example.com")
                .build();
    }

    /**
     * Create an employee for exception testing
     */
    public static Employee createEmployeeForExceptionTest() {
        return EmployeeTestDataBuilder.anEmployee()
                .withName("Exception", "Test")
                .withEmail("exception.test@example.com")
                .build();
    }
}
