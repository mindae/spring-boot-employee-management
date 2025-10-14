package com.example.hello.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        logger.info("Starting database initialization");
        // Initialize test users if they don't exist
        try {
            logger.debug("Checking for existing users in app_users table");
            Integer userCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM app_users", Integer.class);
            logger.debug("Found {} existing users", userCount);
            
            if (userCount == 0) {
                logger.info("No users found, initializing test users");
                
                // Create regular user (password: user)
                jdbcTemplate.update(
                    "INSERT INTO app_users (username, password, enabled, created_date, updated_date) VALUES (?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)",
                    "user", "$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi", 1
                );
                logger.info("Created test user: user (password: user)");
                
                // Create admin user (password: admin)
                jdbcTemplate.update(
                    "INSERT INTO app_users (username, password, enabled, created_date, updated_date) VALUES (?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)",
                    "admin", "$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi", 1
                );
                logger.info("Created admin user: admin (password: admin)");
                
                // Create bill user (password: bill)
                jdbcTemplate.update(
                    "INSERT INTO app_users (username, password, enabled, created_date, updated_date) VALUES (?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)",
                    "bill", "$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi", 1
                );
                logger.info("Created test user: bill (password: bill)");
            } else {
                logger.info("Users already exist, skipping user initialization");
            }
        } catch (Exception e) {
            logger.warn("Failed to initialize users - tables might not exist yet: {}", e.getMessage());
        }
        
        // Initialize test authorities if they don't exist
        try {
            logger.debug("Checking for existing authorities in app_authorities table");
            Integer authorityCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM app_authorities", Integer.class);
            logger.debug("Found {} existing authorities", authorityCount);
            
            if (authorityCount == 0) {
                logger.info("No authorities found, initializing test authorities");
                
                // Regular user role
                jdbcTemplate.update(
                    "INSERT INTO app_authorities (username, authority, created_date) VALUES (?, ?, CURRENT_TIMESTAMP)",
                    "user", "ROLE_USER"
                );
                logger.info("Created authority: user -> ROLE_USER");
                
                // Admin user with both roles
                jdbcTemplate.update(
                    "INSERT INTO app_authorities (username, authority, created_date) VALUES (?, ?, CURRENT_TIMESTAMP)",
                    "admin", "ROLE_USER"
                );
                jdbcTemplate.update(
                    "INSERT INTO app_authorities (username, authority, created_date) VALUES (?, ?, CURRENT_TIMESTAMP)",
                    "admin", "ROLE_ADMIN"
                );
                logger.info("Created authority: admin -> ROLE_USER, ROLE_ADMIN");
                
                // Bill user with admin role
                jdbcTemplate.update(
                    "INSERT INTO app_authorities (username, authority, created_date) VALUES (?, ?, CURRENT_TIMESTAMP)",
                    "bill", "ROLE_ADMIN"
                );
                logger.info("Created authority: bill -> ROLE_ADMIN");
            } else {
                logger.info("Authorities already exist, skipping authority initialization");
            }
        } catch (Exception e) {
            logger.warn("Failed to initialize authorities - tables might not exist yet: {}", e.getMessage());
        }
        
        logger.info("Database initialization completed");
    }
}
