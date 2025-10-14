package com.example.hello.bdd.builders;

import com.example.hello.model.Employee;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Test data builder for Employee entities.
 * Provides fluent API for creating test data with sensible defaults.
 */
public class EmployeeTestDataBuilder {

    private Employee employee;

    public EmployeeTestDataBuilder() {
        this.employee = new Employee();
        // Set sensible defaults
        this.employee.setFirstName("John");
        this.employee.setLastName("Doe");
        this.employee.setEmail("john.doe@example.com");
        this.employee.setPhoneNumber("123-456-7890");
        this.employee.setHireDate(LocalDate.now());
        this.employee.setJobId("IT_PROG");
        this.employee.setSalary(new BigDecimal("50000"));
        this.employee.setCommissionPct(new BigDecimal("0.1"));
        this.employee.setManagerId(100L);
        this.employee.setDepartmentId(10L);
    }

    public static EmployeeTestDataBuilder anEmployee() {
        return new EmployeeTestDataBuilder();
    }

    public EmployeeTestDataBuilder withId(Long id) {
        this.employee.setId(id);
        return this;
    }

    public EmployeeTestDataBuilder withName(String firstName, String lastName) {
        this.employee.setFirstName(firstName);
        this.employee.setLastName(lastName);
        return this;
    }

    public EmployeeTestDataBuilder withEmail(String email) {
        this.employee.setEmail(email);
        return this;
    }

    public EmployeeTestDataBuilder withPhoneNumber(String phoneNumber) {
        this.employee.setPhoneNumber(phoneNumber);
        return this;
    }

    public EmployeeTestDataBuilder withHireDate(LocalDate hireDate) {
        this.employee.setHireDate(hireDate);
        return this;
    }

    public EmployeeTestDataBuilder withJobId(String jobId) {
        this.employee.setJobId(jobId);
        return this;
    }

    public EmployeeTestDataBuilder withSalary(BigDecimal salary) {
        this.employee.setSalary(salary);
        return this;
    }

    public EmployeeTestDataBuilder withCommissionPct(BigDecimal commissionPct) {
        this.employee.setCommissionPct(commissionPct);
        return this;
    }

    public EmployeeTestDataBuilder withManagerId(Long managerId) {
        this.employee.setManagerId(managerId);
        return this;
    }

    public EmployeeTestDataBuilder withDepartmentId(Long departmentId) {
        this.employee.setDepartmentId(departmentId);
        return this;
    }

    public EmployeeTestDataBuilder withInvalidData() {
        this.employee.setFirstName(""); // Invalid: empty first name
        this.employee.setLastName("Doe");
        this.employee.setEmail("invalid-email"); // Invalid: no @ symbol
        this.employee.setSalary(new BigDecimal("-1000")); // Invalid: negative salary
        return this;
    }

    public EmployeeTestDataBuilder asDeveloper() {
        this.employee.setJobId("IT_PROG");
        this.employee.setSalary(new BigDecimal("75000"));
        this.employee.setDepartmentId(60L); // IT department
        return this;
    }

    public EmployeeTestDataBuilder asManager() {
        this.employee.setJobId("MANAGER");
        this.employee.setSalary(new BigDecimal("100000"));
        this.employee.setDepartmentId(10L); // Administration department
        return this;
    }

    public EmployeeTestDataBuilder asSalesRep() {
        this.employee.setJobId("SA_REP");
        this.employee.setSalary(new BigDecimal("60000"));
        this.employee.setCommissionPct(new BigDecimal("0.15"));
        this.employee.setDepartmentId(80L); // Sales department
        return this;
    }

    public Employee build() {
        return this.employee;
    }
}
