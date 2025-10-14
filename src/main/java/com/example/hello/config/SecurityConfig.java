package com.example.hello.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
	private final UserDetailsService userDetailsService;

	public SecurityConfig(UserDetailsService userDetailsService) {
		logger.info("Initializing SecurityConfig with UserDetailsService");
		this.userDetailsService = userDetailsService;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		logger.info("Configuring security filter chain with Basic Auth and Form Login");
		
		http
			.userDetailsService(userDetailsService)
			.authorizeHttpRequests(auth -> auth
				// Public endpoints
				.requestMatchers("/login").permitAll()
				.requestMatchers("/login.html").permitAll()
				.requestMatchers("/error", "/favicon.ico", "/webjars/**", "/css/**", "/js/**").permitAll()
				// API documentation endpoints
				.requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
				// Health check endpoint
				.requestMatchers("/actuator/health").permitAll()
				// All other requests require authentication
				.anyRequest().authenticated()
			)
			// Configure HTTP Basic Authentication for API endpoints
			.httpBasic(basic -> basic
				.realmName("Employee Management API")
			)
			// Configure form-based authentication for web interface
			.formLogin(form -> form
				.loginPage("/login")
				.loginProcessingUrl("/login")
				.defaultSuccessUrl("/swagger-ui/index.html", true)
				.failureUrl("/login?error")
				.permitAll()
			)
			.logout(logout -> logout
				.logoutUrl("/logout")
				.logoutSuccessUrl("/login?logout")
				.permitAll()
			)
			// Disable CSRF for API endpoints but keep it for form-based auth
			.csrf(csrf -> csrf
				.ignoringRequestMatchers("/api/**", "/v3/api-docs/**", "/swagger-ui/**")
			);
		
		logger.info("Security filter chain configured with Basic Auth and Form Login");
		return http.build();
	}
}


