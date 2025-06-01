package com.shubhajit.todotask.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI/Swagger configuration for the To-Do Task API.
 */
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI todoTaskOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("To-Do Task API")
                        .description("Spring Boot REST API for managing to-do tasks.")
                        .version("v1.0.0")
                        .license(new License().name("Demo License").url("https://github.com/shubhajit1992")))
                .externalDocs(new ExternalDocumentation()
                        .description("Project Repository")
                        .url("https://github.com/shubhajit1992/to-do-task"));
    }
}
