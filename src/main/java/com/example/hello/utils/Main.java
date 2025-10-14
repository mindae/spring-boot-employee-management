package com.example.hello.utils;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Main {

    public static void main(String[] args) {
        String jdbcUrl = "jdbc:oracle:thin:@localhost:1521/XEPDB1"; // Change if needed
        String username = "hr"; // HR user you set up
        String password = "hr"; // Replace with actual password

        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password)) {
            System.out.println(" Connected to Oracle HR schema!");

            String sql = "SELECT EMPLOYEE_ID, FIRST_NAME, SALARY FROM EMPLOYEES";

            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    int empId = rs.getInt("EMPLOYEE_ID");
                    String firstName = rs.getString("FIRST_NAME");
                    double salary = rs.getDouble("SALARY");

                    System.out.printf("ID: %d, Name: %s, Salary: %.2f%n", empId, firstName, salary);
                }
            }

        } catch (Exception e) {
            System.err.println(" Database connection failed:");
            e.printStackTrace();
        }
    }
}

