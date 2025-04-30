package com.frogcrew.frogcrew_backend.crewmember;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface  UserRepository extends JpaRepository<CrewMemberUser, Integer> {
    Optional<CrewMemberUser> findByEmail(String email);

}
