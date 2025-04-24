package com.frogcrew.frogcrew_backend.crewmember.invite;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class InvitationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @Column(unique = true,nullable = false)
    private String token;

    @Column(nullable = false)
    private String email;

    private boolean used  = false;

    private LocalDateTime expiresAt;


    public InvitationToken( String token, String email, boolean used, LocalDateTime expiresAt) {
        this.token = token;
        this.email = email;
        this.used = used;
        this.expiresAt = expiresAt;

    }

    public InvitationToken() {

    }


    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
}
