package com.frogcrew.frogcrew_backend.security;

// Required for accessing HTTP request and response
import com.frogcrew.frogcrew_backend.crewmember.client.RedisCacheClient;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Security classes to handle auth context and JWT
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component // Marks this as a Spring-managed interceptor bean
public class JwtInterceptor implements HandlerInterceptor {

    private final RedisCacheClient redisCacheClient;

    // Inject the RedisCacheClient to check the token whitelist
    public JwtInterceptor(RedisCacheClient redisCacheClient) {
        this.redisCacheClient = redisCacheClient;
    }

    /**
     * This method is automatically called before every controller request.
     * It checks whether the request includes a valid JWT that exists in Redis whitelist.
     * If the token is not valid or expired (i.e., removed from Redis), it throws a BadCredentialsException.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Grab the "Authorization" header from the incoming request
        String authorizationHeader = request.getHeader("Authorization");

        // Check if it's a Bearer token — this means it's a protected route
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

            // Get the current authenticated user and their JWT
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Jwt jwt = (Jwt) authentication.getPrincipal();

            // Extract the userId claim from the JWT
            String userId = jwt.getClaim("userId").toString();

            // Use Redis to confirm if this token is still in the whitelist
            if (!this.redisCacheClient.isUserTokenInWhiteList(userId, jwt.getTokenValue())) {
                // If not, the token is invalid (maybe expired, blacklisted, etc.)
                throw new BadCredentialsException("Invalid token");
            }
        }

        // Allow the request to proceed
        return true;
    }
}
