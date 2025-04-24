package com.frogcrew.frogcrew_backend.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

/**
 * This class is triggered when a JWT-based authentication attempt fails.
 *
 * For example:
 * - A client makes a request to a protected endpoint without a token.
 * - A client provides an invalid or expired token.
 *
 * Spring Security will delegate to this class to handle the failure gracefully.
 */
@Component
public class CustomBearerTokenAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * The HandlerExceptionResolver allows us to delegate exception handling
     * to a centralized error-handling mechanism, like @ControllerAdvice.
     *
     * This is useful because it decouples security exception logic from the security layer itself.
     * By injecting the default `HandlerExceptionResolver`, we can reuse our global exception strategy.
     */
    private final HandlerExceptionResolver resolver;

    /**
     * Constructor injection for the exception resolver.
     * The @Qualifier ensures the correct default resolver bean is injected.
     */
    public CustomBearerTokenAuthenticationEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    /**
     * This method is automatically called by Spring Security when an unauthenticated
     * request tries to access a secured endpoint.
     *
     * @param request The HTTP request that caused the authentication failure.
     * @param response The HTTP response being built.
     * @param authException The actual exception describing the failure (e.g., missing token).
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        // Delegate to the global exception handler (e.g., one defined with @ControllerAdvice)
        this.resolver.resolveException(request, response, null, authException);
    }
}
