package com.frogcrew.frogcrew_backend.security;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frogcrew.frogcrew_backend.system.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomBearerTokenAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper(); // For JSON serialization

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException {

        // Create a structured error response using the Result class
        Result errorResult = new Result(
                false,                       // `flag`: Denied access
                HttpServletResponse.SC_FORBIDDEN, // `code`: 403 Forbidden
                "You do not have sufficient permissions to access this resource", // Custom error message
                null                        // `data`: No extra data in this case
        );

        // Set HTTP response properties
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Write the response as JSON
        response.getWriter().write(objectMapper.writeValueAsString(errorResult));
    }
}