package com.frogcrew.frogcrew_backend.invite;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvitationRepository extends JpaRepository<InvitationToken, Integer> {
    Optional<InvitationToken> findByToken(String token); // make sure token is not null
}
