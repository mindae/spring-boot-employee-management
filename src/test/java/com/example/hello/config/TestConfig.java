package com.example.hello.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import javax.sql.DataSource;

/**
 * Test configuration for BDD testing.
 * Provides test-specific configuration for integration testing.
 */
@TestConfiguration
@Profile("test")
public class TestConfig {

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        // Use H2 in-memory database for testing
        registry.add("spring.datasource.url", () -> "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
        registry.add("spring.datasource.username", () -> "sa");
        registry.add("spring.datasource.password", () -> "");
        registry.add("spring.datasource.driver-class-name", () -> "org.h2.Driver");
        
        // Disable security for testing
        registry.add("spring.security.user.name", () -> "test");
        registry.add("spring.security.user.password", () -> "test");
        registry.add("spring.security.user.roles", () -> "USER");
    }

    @Bean
    @Primary
    public DataSource testDataSource() {
        return new org.springframework.jdbc.datasource.SimpleDriverDataSource(
            new org.h2.Driver(),
            "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
            "sa",
            ""
        );
    }

    @Bean
    public JdbcTemplate testJdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
