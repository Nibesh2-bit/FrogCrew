package com.frogcrew.frogcrew_backend.security;
// This annotation marks the class as a configuration class, like a Spring-managed settings file
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// These are needed to set up CORS rules (Cross-Origin Resource Sharing)
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // This tells Spring Boot that this class contains configuration settings
public class CorsConfiguration {

    // This method registers a bean that customizes CORS behavior for the whole app
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // This allows all routes in your backend to be accessible via CORS
                registry.addMapping("/**") // Apply this rule to all endpoints
                        .allowedOrigins("*") // Allow requests from any origin (frontend, mobile app, etc.)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH") // Support common HTTP methods
                        .allowedHeaders("*"); // Allow any custom headers (like Authorization, Content-Type, etc.)
            }
        };
    }
}
