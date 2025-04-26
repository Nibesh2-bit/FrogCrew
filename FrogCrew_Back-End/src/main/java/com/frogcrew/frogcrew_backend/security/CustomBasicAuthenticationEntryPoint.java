package com.frogcrew.frogcrew_backend.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.frogcrew.frogcrew_backend.system.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class CustomBasicAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final HandlerExceptionResolver resolver;
    private final ObjectMapper objectMapper = new ObjectMapper(); // For serializing objects to JSON

    public CustomBasicAuthenticationEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
//
//        // Add the "WWW-Authenticate" header required by basic authentication
//        response.addHeader("WWW-Authenticate", "Basic realm=\"Realm\"");
//
//        // Build a structured error response with Result
//        Result errorResult = new Result(
//                false,                        // `flag`: authentication failed
//                HttpServletResponse.SC_UNAUTHORIZED, // `code`: HTTP 401 Unauthorized
//                "Authentication failed: " + authException.getMessage(), // Custom error message
//                null                          // `data`: no additional data
//        );
//
//        // Set response properties
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // HTTP 401 Unauthorized
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//
//        // Write the error response as JSON
//        response.getWriter().write(objectMapper.writeValueAsString(errorResult));

        response.addHeader("WWW-Authenticate", "Basic realm=\"Realm\"");

        // Delegate to the global exception handler
        this.resolver.resolveException(request, response, null, authException);

    }
}