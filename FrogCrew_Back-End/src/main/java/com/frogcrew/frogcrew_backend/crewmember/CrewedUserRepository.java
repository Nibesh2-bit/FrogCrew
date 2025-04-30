package com.frogcrew.frogcrew_backend.crewmember;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CrewedUserRepository extends JpaRepository<CrewList, Integer>{
    // Find CrewedUser by userId
    CrewedUser findByUserId(Integer userId);

    // Find all CrewedUsers by gameId (as gameId is not unique!)
    Optional<CrewedUser> findByGameId(Integer gameId);

    // Find all CrewedUsers by fullName
    Optional<CrewedUser> findByFullName(String fullName);

    // Find all CrewedUsers by position
    Optional<CrewedUser> findByPosition(String position);

    // Find all CrewedUsers by reportTime
    Optional<CrewedUser> findByReportTime(String reportTime);

    // combining search keys
    Optional<CrewedUser> findByGameIdAndPosition(Integer gameId, String position);


}
