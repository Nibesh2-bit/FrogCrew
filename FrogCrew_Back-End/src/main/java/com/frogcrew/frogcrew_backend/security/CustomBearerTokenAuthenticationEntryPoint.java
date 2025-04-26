package com.frogcrew.frogcrew_backend.security;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frogcrew.frogcrew_backend.system.Result;
import com.frogcrew.frogcrew_backend.system.StatusCode;
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
public class CustomBearerTokenAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final HandlerExceptionResolver resolver;
    private final ObjectMapper objectMapper = new ObjectMapper(); // For converting objects to JSON

    public CustomBearerTokenAuthenticationEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        // Build the error response using the Result class
        Result errorResult = new Result(
                false,                       // `flag`: authentication failed
                StatusCode.UNAUTHORIZED, // `code`: HTTP 401
                "username or password incorrect", // Error message
                null                        // `data`: no additional data
        );

        // Set response properties
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Write the JSON response to the output
        response.getWriter().write(objectMapper.writeValueAsString(errorResult));
    }
}