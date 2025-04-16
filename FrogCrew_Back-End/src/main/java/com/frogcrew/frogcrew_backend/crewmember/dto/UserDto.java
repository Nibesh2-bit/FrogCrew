package com.frogcrew.frogcrew_backend.crewmember.dto;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;

public record UserDto (

     Integer id,

    @NotEmpty(message = "username is required.")
    String username,

    @NotEmpty(message = "password is required.")
    String password,

    @NotEmpty(message = "email is required.")
    String email){


}
