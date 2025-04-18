package com.frogcrew.frogcrew_backend.crewmember;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface  UserRepository extends JpaRepository<CrewMemberUser, Integer> {
    Optional<CrewMemberUser> findByEmail(String email);

}
