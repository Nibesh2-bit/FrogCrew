package com.frogcrew.frogcrew_backend.security;

// Spring Security classes for handling authentication and JWT claims
import com.frogcrew.frogcrew_backend.crewmember.MyUserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Component // This marks the class as a Spring-managed bean (like a service, but lower level)
public class JwtProvider {

    private final JwtEncoder jwtEncoder;

    // Inject the JwtEncoder (provided by Spring Security's OAuth2 library)
    public JwtProvider(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    /**
     * This method creates a JWT for an authenticated user.
     * It pulls details like the username, roles, and userId,
     * then adds them to the JWT claims.
     *
     * @param authentication - contains the currently authenticated user info.
     * @return JWT token as a string.
     */
    public String createToken(Authentication authentication) {
        // Get current timestamp
        Instant now = Instant.now();

        // Set token validity duration (2 hours from now)
        long expiresIn = 2;

        // Get the list of user authorities (roles), and join them with space as a delimiter
        // This will look like "ROLE_ADMIN ROLE_USER"
        String authorities = authentication.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .collect(Collectors.joining(" ")); // Required to be space-separated for the system

        // Construct the payload (claims) of the JWT
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self") // Who issued the token
                .issuedAt(now) // When the token was issued
                .expiresAt(now.plus(expiresIn, ChronoUnit.HOURS)) // Expiry time
                .subject(authentication.getName()) // Usually the username
                .claim("userId", ((MyUserPrincipal)(authentication.getPrincipal())).getCrewMemberUser().getId())
                // Custom claim: userId from your custom principal object
                .claim("authorities", authorities) // Custom claim: roles
                .build();

        // Encode the JWT claims into a signed token string and return it
        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

}
