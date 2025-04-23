package com.frogcrew.frogcrew_backend.crewmember.dto;



import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CrewMemberDto (

        Integer id,
        @NotEmpty(message = "Firstname is required.")
        String firstName,
        @NotEmpty(message = "Lastname is required.")
        String lastName,
        @NotEmpty(message = "Email is required.")
        String email,
        @NotEmpty(message = "Phone number is required.")
        String phoneNumber,
        @NotEmpty(message = "Password is required.")
        String password,

        @NotEmpty(message = "Role is required.")
        String role,
        @NotEmpty(message = "Positions is required")
        List<String> positions)



//    @NotEmpty(message = "password is required.")
//    String password,


{

}
