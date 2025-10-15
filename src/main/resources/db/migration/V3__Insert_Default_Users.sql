-- =====================================================
-- Flyway Migration: V3__Insert_Default_Users.sql
-- Description: Insert default users and authorities for testing
-- Author: Mahendra Chaurasia
-- Date: 2025-10-15
-- =====================================================

-- Insert default users (passwords are BCrypt encrypted)
-- Password for all users: same as username (user/user, admin/admin, bill/bill)
-- BCrypt hash for 'user': $2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi
-- BCrypt hash for 'admin': $2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi
-- BCrypt hash for 'bill': $2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi

-- Insert regular user
INSERT INTO APP_USERS (USERNAME, PASSWORD, ENABLED, CREATED_DATE, UPDATED_DATE) 
VALUES ('user', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert admin user
INSERT INTO APP_USERS (USERNAME, PASSWORD, ENABLED, CREATED_DATE, UPDATED_DATE) 
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert bill user
INSERT INTO APP_USERS (USERNAME, PASSWORD, ENABLED, CREATED_DATE, UPDATED_DATE) 
VALUES ('bill', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert authorities for regular user
INSERT INTO APP_AUTHORITIES (USERNAME, AUTHORITY, CREATED_DATE) 
VALUES ('user', 'ROLE_USER', CURRENT_TIMESTAMP);

-- Insert authorities for admin user (both ROLE_USER and ROLE_ADMIN)
INSERT INTO APP_AUTHORITIES (USERNAME, AUTHORITY, CREATED_DATE) 
VALUES ('admin', 'ROLE_USER', CURRENT_TIMESTAMP);

INSERT INTO APP_AUTHORITIES (USERNAME, AUTHORITY, CREATED_DATE) 
VALUES ('admin', 'ROLE_ADMIN', CURRENT_TIMESTAMP);

-- Insert authorities for bill user (ROLE_ADMIN)
INSERT INTO APP_AUTHORITIES (USERNAME, AUTHORITY, CREATED_DATE) 
VALUES ('bill', 'ROLE_ADMIN', CURRENT_TIMESTAMP);
