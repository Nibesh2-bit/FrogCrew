package com.frogcrew.frogcrew_backend.security.Auth.DTO;

public class LoginResponseDTO {
       private String email;
       private String token;

       // Add other necessary fields like roles, name, etc., as needed
       private String firstName;
       private String lastName;

       public LoginResponseDTO(String email, String token, String firstName, String lastName) {
           this.email = email;
           this.token = token;
           this.firstName = firstName;
           this.lastName = lastName;
       }

       // Getters and setters
       public String getEmail() {
           return email;
       }

       public void setEmail(String email) {
           this.email = email;
       }

       public String getToken() {
           return token;
       }

       public void setToken(String token) {
           this.token = token;
       }

       public String getFirstName() {
           return firstName;
       }

       public void setFirstName(String firstName) {
           this.firstName = firstName;
       }

       public String getLastName() {
           return lastName;
       }

       public void setLastName(String lastName) {
           this.lastName = lastName;
       }
   }