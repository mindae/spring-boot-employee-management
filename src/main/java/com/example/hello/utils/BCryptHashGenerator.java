package com.example.hello.utils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptHashGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // Generate hashes for test users
        String userPassword = "user";
        String billPassword = "bill";
        
        String userHash = encoder.encode(userPassword);
        String billHash = encoder.encode(billPassword);

        System.out.println("=== BCrypt Hashes for Database ===");
        System.out.println("User 'user' password hash: " + userHash);
        System.out.println("User 'bill' password hash: " + billHash);
        System.out.println();
        System.out.println("=== SQL INSERT statements ===");
        System.out.println("INSERT INTO users (username, password, enabled) VALUES ('user', '" + userHash + "', 1);");
        System.out.println("INSERT INTO users (username, password, enabled) VALUES ('bill', '" + billHash + "', 1);");
    }
}

