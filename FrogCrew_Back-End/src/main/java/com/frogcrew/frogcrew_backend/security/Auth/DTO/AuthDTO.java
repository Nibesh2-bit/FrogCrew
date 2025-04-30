package com.frogcrew.frogcrew_backend.security.Auth.DTO;

import jakarta.validation.constraints.NotEmpty;



public record AuthDTO(
        @NotEmpty(message = "userId is required.") Integer userID,
        @NotEmpty(message = "Role is required.") String role,
        @NotEmpty(message = "Token is required.") String token
)
{

}
