package com.example.hello.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
			.info(new Info()
				.title("Employee Management System API")
				.description("REST API for managing employees with authentication support")
				.version("v1.0")
				.contact(new Contact()
					.name("Employee Management Team")
					.email("admin@example.com")
				)
				.license(new License()
					.name("MIT License")
					.url("https://opensource.org/licenses/MIT")
				)
			)
			.addSecurityItem(new SecurityRequirement().addList("basicAuth"))
			.components(new Components()
				.addSecuritySchemes("basicAuth", new SecurityScheme()
					.type(SecurityScheme.Type.HTTP)
					.scheme("basic")
					.description("HTTP Basic Authentication. Use your username and password.")
				)
			);
	}
}


