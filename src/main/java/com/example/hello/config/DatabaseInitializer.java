package com.example.hello.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Database Initialization Service
 * 
 * This component is now simplified since database initialization
 * is handled by Flyway migrations in src/main/resources/db/migration/
 * 
 * @author Mahendra Chaurasia
 * @version 1.0.0
 */
@Component
public class DatabaseInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

    @Override
    public void run(String... args) throws Exception {
        logger.info("Database initialization service started");
        logger.info("Database schema and data are managed by Flyway migrations");
        logger.info("Check src/main/resources/db/migration/ for migration files");
        logger.info("Database initialization service completed");
    }
}