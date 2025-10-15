-- =====================================================
-- Flyway Migration: V2__Create_Employee_Table.sql
-- Description: Create employee management table
-- Author: Mahendra Chaurasia
-- Date: 2025-10-15
-- =====================================================

-- Create EMPLOYEES table for employee management
CREATE TABLE EMPLOYEES (
    EMPLOYEE_ID NUMBER(19) NOT NULL PRIMARY KEY,
    FIRST_NAME VARCHAR2(50),
    LAST_NAME VARCHAR2(50),
    EMAIL VARCHAR2(100),
    PHONE_NUMBER VARCHAR2(20),
    HIRE_DATE DATE,
    JOB_ID VARCHAR2(20),
    SALARY NUMBER(10,2),
    COMMISSION_PCT NUMBER(3,2),
    MANAGER_ID NUMBER(19),
    DEPARTMENT_ID NUMBER(19)
);

-- Create indexes for better performance
CREATE INDEX IDX_EMPLOYEES_EMAIL ON EMPLOYEES(EMAIL);
CREATE INDEX IDX_EMPLOYEES_MANAGER_ID ON EMPLOYEES(MANAGER_ID);
CREATE INDEX IDX_EMPLOYEES_DEPARTMENT_ID ON EMPLOYEES(DEPARTMENT_ID);
CREATE INDEX IDX_EMPLOYEES_JOB_ID ON EMPLOYEES(JOB_ID);

-- Add comments for documentation
COMMENT ON TABLE EMPLOYEES IS 'Employee information table';

COMMENT ON COLUMN EMPLOYEES.EMPLOYEE_ID IS 'Primary key - Employee ID';
COMMENT ON COLUMN EMPLOYEES.FIRST_NAME IS 'Employee first name';
COMMENT ON COLUMN EMPLOYEES.LAST_NAME IS 'Employee last name';
COMMENT ON COLUMN EMPLOYEES.EMAIL IS 'Employee email address';
COMMENT ON COLUMN EMPLOYEES.PHONE_NUMBER IS 'Employee phone number';
COMMENT ON COLUMN EMPLOYEES.HIRE_DATE IS 'Employee hire date';
COMMENT ON COLUMN EMPLOYEES.JOB_ID IS 'Employee job identifier';
COMMENT ON COLUMN EMPLOYEES.SALARY IS 'Employee salary';
COMMENT ON COLUMN EMPLOYEES.COMMISSION_PCT IS 'Commission percentage';
COMMENT ON COLUMN EMPLOYEES.MANAGER_ID IS 'Manager employee ID (self-reference)';
COMMENT ON COLUMN EMPLOYEES.DEPARTMENT_ID IS 'Department identifier';
