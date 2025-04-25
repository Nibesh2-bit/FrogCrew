package com.frogcrew.frogcrew_backend.security.Auth;

import com.frogcrew.frogcrew_backend.crewmember.CrewMemberUser;
import com.frogcrew.frogcrew_backend.crewmember.MyUserPrincipal;
import com.frogcrew.frogcrew_backend.crewmember.converter.UserToUserDtoConverter;
import com.frogcrew.frogcrew_backend.crewmember.dto.UserDto;
import com.frogcrew.frogcrew_backend.security.JwtProvider;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * AuthService processes login responses and generates JWT tokens without Redis whitelisting.
 */
@Service
public class AuthService {

    // Responsible for generating JWT tokens
    private final JwtProvider jwtProvider;

    // Converts CrewMemberUser entity into a DTO for safe usage
    private final UserToUserDtoConverter userToUserDtoConverter;

    // Inject dependencies via constructor
    public AuthService(JwtProvider jwtProvider, UserToUserDtoConverter userToUserDtoConverter) {
        this.jwtProvider = jwtProvider;
        this.userToUserDtoConverter = userToUserDtoConverter;
    }

    /**
     * This method creates login information and generates a JWT token for a successfully authenticated user.
     *
     * @param authentication — Spring Security's Authentication object (provided after login).
     * @return Map containing user information and the JWT token.
     */
    public Map<String, Object> createLoginInfo(Authentication authentication) {
        // Step 1: Extract the user (MyUserPrincipal is your custom UserDetails implementation)
        MyUserPrincipal principal = (MyUserPrincipal) authentication.getPrincipal();
        CrewMemberUser crewMemberUser = principal.getCrewMemberUser();

        // Step 2: Convert the full user entity into a safe UserDto (to exclude sensitive data, e.g., password)
        UserDto userDto = this.userToUserDtoConverter.convert(crewMemberUser);

        // Step 3: Generate a JWT token for the user
        String token = this.jwtProvider.createToken(authentication);

        // Step 4: Prepare the response object
        Map<String, Object> loginResultMap = new HashMap<>();
        loginResultMap.put("userInfo", userDto); // Public user information
        loginResultMap.put("token", token);      // JWT token

        return loginResultMap;
    }
}