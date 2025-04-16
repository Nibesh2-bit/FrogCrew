package com.frogcrew.frogcrew_backend.crewmember;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "users")
public class CrewMemberUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotEmpty(message = "username is required.")
    private String username;

    @NotEmpty(message = "password is required.")
    private String password;

    @NotEmpty(message = "email is required.")
    private String email;

    public CrewMemberUser( String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public @NotEmpty(message = "username is required.") String getUsername() {
        return username;
    }

    public void setUsername(@NotEmpty(message = "username is required.") String username) {
        this.username = username;
    }

    public @NotEmpty(message = "password is required.") String getPassword() {
        return password;
    }

    public void setPassword(@NotEmpty(message = "password is required.") String password) {
        this.password = password;
    }

    public @NotEmpty(message = "email is required.") String getEmail() {
        return email;
    }

    public void setEmail(@NotEmpty(message = "email is required.") String email) {
        this.email = email;
    }

    public CrewMemberUser() {
        super();
    }




}
