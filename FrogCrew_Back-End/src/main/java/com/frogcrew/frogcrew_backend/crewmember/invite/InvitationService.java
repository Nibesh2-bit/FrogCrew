package com.frogcrew.frogcrew_backend.crewmember.invite;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.lang.module.ResolutionException;
import java.time.LocalDateTime;

@Service
public class InvitationService {

    private final InvitationRepository invitationRepository;

    public InvitationService(InvitationRepository invitationRepository) {
        this.invitationRepository = invitationRepository;
    }

    public InvitationToken validateToken(String token, String email) {
//        InvitationToken invitationToken = invitationRepository.findByToken(token)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invitation not valid"));
//
//        if (invitationToken.isUsed()) {
//            throw new IllegalStateException("This invitation token has already been used.");
//        }
//
//        if (invitationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
//            throw new IllegalStateException("This invitation token has expired.");
//        }
                // 1. Check if the token exists
        System.out.println("Validating token: " + token);
        InvitationToken invitationToken = invitationRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invitation not valid"));

        // 2. Check if the token is used or expired
        System.out.println("Token found. Token expiry: " + invitationToken.getExpiresAt());
        if (invitationToken.isUsed() || invitationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token expired or already used");
        }

        // 3. Check if email in the token matches the submitted one
        if (!invitationToken.getEmail().equals(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email does not match invitation");
        }

        return invitationToken;
    }

}