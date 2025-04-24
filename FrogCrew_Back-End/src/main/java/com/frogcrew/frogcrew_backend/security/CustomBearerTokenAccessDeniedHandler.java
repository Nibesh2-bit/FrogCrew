package com.frogcrew.frogcrew_backend.security;



import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

/**
 * This class handles *unsuccessful authorization* using JWT Bearer tokens.
 * It implements Spring Security's AccessDeniedHandler interface, which gets triggered when:
 * - The user is authenticated (i.e., has a valid JWT),
 * - BUT tries to access a resource for which they don't have sufficient permissions/roles.
 */
@Component
public class CustomBearerTokenAccessDeniedHandler implements AccessDeniedHandler {

    // This is Spring's default mechanism for resolving exceptions.
    // It allows us to send a consistent error response, often formatted as JSON, instead of a raw 403 page.
    private final HandlerExceptionResolver resolver;

    /**
     * Constructor-based dependency injection.
     * The @Qualifier ensures the correct HandlerExceptionResolver bean is injected.
     */
    public CustomBearerTokenAccessDeniedHandler(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    /**
     * This method gets called automatically when an AccessDeniedException is thrown by Spring Security.
     *
     * @param request               the HTTP request being processed
     * @param response              the HTTP response object
     * @param accessDeniedException the exception thrown because of insufficient permissions
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        // Delegate the exception handling to Spring's global exception handler
        // so that it can be processed in a @ControllerAdvice, or other centralized error logic.
        this.resolver.resolveException(request, response, null, accessDeniedException);
    }
}
