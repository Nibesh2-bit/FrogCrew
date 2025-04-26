package com.frogcrew.frogcrew_backend.crewmember.dto;



import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record UserDto (

     Integer id,
    @NotEmpty(message = "Firstname is required")
    String firstName,
    @NotEmpty(message = "Lastname is required")
    String lastName,
     @Email(message = "Invalid Email")
     @NotEmpty(message = "Email is required")
     String email,
    @NotEmpty(message = "Phone number is required")
     @Pattern(regexp = "\\d{3}\\d{3}\\d{4}", message = "Phone Number format must be 999-999-9999")
    String phoneNumber,

@NotEmpty(message = "Role is required")
String role,
@NotEmpty(message = "Positions is required")
     List<String> positions)



//    @NotEmpty(message = "password is required.")
//    String password,


        {

}
