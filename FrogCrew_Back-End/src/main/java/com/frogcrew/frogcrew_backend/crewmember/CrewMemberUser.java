package com.frogcrew.frogcrew_backend.crewmember;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
//@Table(name = "users")
public class CrewMemberUser  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @NotEmpty(message = "firstname is required.")
    private String firstName;

    @NotEmpty(message = "Lastname is required.")
    private String lastName;
    @NotEmpty(message = "Email is required.")
    private String email;

    @NotEmpty(message = "Password is required.")
    private String password;


    @NotEmpty(message = "Phone number is required.")
    private String phoneNumber;

    @NotEmpty(message = "Role is required.")
    private String role;


    @ElementCollection //join a list as an entity
    @CollectionTable(name = "crew_member_positions", joinColumns = @JoinColumn(name = "crew_member_id"))
    @Column(name = "position")
    @NotEmpty(message = "Positions is required.")
    private List<String> positions = new ArrayList<>();

    public CrewMemberUser(Integer id, String firstName, String lastName, String email, String password, String phoneNumber, String role, List<String> positions) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.positions = positions;
    }

    public CrewMemberUser() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public @NotEmpty(message = "firstname is required.") String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotEmpty(message = "firstname is required.") String firstName) {
        this.firstName = firstName;
    }

    public @NotEmpty(message = "Lastname is required.") String getLastName() {
        return lastName;
    }

    public void setLastName(@NotEmpty(message = "Lastname is required.") String lastName) {
        this.lastName = lastName;
    }

    public @NotEmpty(message = "Email is required.") String getEmail() {
        return email;
    }

    public void setEmail(@NotEmpty(message = "Email is required.") String email) {
        this.email = email;
    }

    public @NotEmpty(message = "Password is required.") String getPassword() {
        return password;
    }

    public void setPassword(@NotEmpty(message = "Password is required.") String password) {
        this.password = password;
    }

    public @NotEmpty(message = "Phone number is required.") String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@NotEmpty(message = "Phone number is required.") String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public @NotEmpty(message = "Role is required.") String getRole() {
        return role;
    }

    public void setRole(@NotEmpty(message = "Role is required.") String role) {
        this.role = role;
    }

    public @NotEmpty(message = "Positions is required.") List<String> getPositions() {
        return positions;
    }

    public void setPositions(@NotEmpty(message = "Positions is required.") List<String> positions) {
        this.positions = positions;
    }

    //    public CrewMemberUser() {
//        super();
//    }




}
