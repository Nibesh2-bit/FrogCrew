package com.frogcrew.frogcrew_backend.security;

import com.frogcrew.frogcrew_backend.system.Result;
import com.frogcrew.frogcrew_backend.system.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AuthController is responsible for handling login requests.
 * It lives at the endpoint: /api/v1/auth/login (thanks to @RequestMapping and  yml config).
 *
 * This controller works *after* Spring Security successfully authenticates credentials via Basic Auth.
 */
@RestController
@RequestMapping("${api.endpoint.base-url}/users") // Resolves to /api/v1/users
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    // Service that generates JWT tokens and user info to return on login
    private final AuthService authService;

    // Inject the service through constructor
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     *This endpoint is hit after Basic Authentication is successful.
     * Endpoint: POST /api/v1/users/login
     *
     * Spring Security automatically verifies the username & password
     * and passes the authenticated user into this method via the `Authentication` object.
     *
     * Objectives:
     * - Log the username
     * - Ask AuthService to generate login response (token + user data)
     * - Wrap that in a `Result` (standard response format)
     *

     * Use cases like:
     *   - Crew member registration
     *   - Viewing schedules
     *   - Viewing crew profiles
     *   - Admin-only operations
     * ...all rely on having this token returned here.
     */
    @PostMapping("/login")
    public Result getLoginInfo(Authentication authentication) {
        LOGGER.debug("Authenticated user: '{}'", authentication.getName());

        // Generate and return a JWT + user info using your auth service.
        return new Result(
                true,
                StatusCode.SUCCESS,
                "User Info and JSON Web Token",
                this.authService.createLoginInfo(authentication)
        );
    }

}