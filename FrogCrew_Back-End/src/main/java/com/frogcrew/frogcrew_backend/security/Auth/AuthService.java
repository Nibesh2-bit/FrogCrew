package com.frogcrew.frogcrew_backend.security.Auth;

import com.frogcrew.frogcrew_backend.security.Auth.DTO.AuthDTO;
import com.frogcrew.frogcrew_backend.crewmember.MyUserPrincipal;
import com.frogcrew.frogcrew_backend.security.JwtProvider;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final JwtProvider jwtProvider;

    public AuthService(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    public AuthDTO createLoginInfo(Authentication authentication) {
        // Extract user details from the principal
        MyUserPrincipal principal = (MyUserPrincipal) authentication.getPrincipal();
        Integer userId = principal.getCrewMemberUser().getId();
        String role = authentication.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .findFirst()
                .orElse(null);

        // Generate JWT Bearer token
        String token = jwtProvider.createToken(authentication);

        // Return user information and token
        return new AuthDTO(userId, role, token);
    }
}