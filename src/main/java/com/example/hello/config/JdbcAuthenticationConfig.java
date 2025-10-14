package com.example.hello.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;

@Configuration
public class JdbcAuthenticationConfig {

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
        
        // Configure custom table names and column names
        manager.setUsersByUsernameQuery(
            "SELECT username, password, enabled FROM app_users WHERE username = ?"
        );
        
        manager.setAuthoritiesByUsernameQuery(
            "SELECT username, authority FROM app_authorities WHERE username = ?"
        );
        
        return manager;
    }
}
