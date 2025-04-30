package com.frogcrew.frogcrew_backend.crewmember;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CrewedUserRepository extends JpaRepository<CrewList, Integer>{
    // Find CrewedUser by userId
    CrewedUser findByUserId(Integer userId);

    // Find all CrewedUsers by gameId (as gameId is not unique!)
    List<CrewedUser> findByGameId(Integer gameId);

    // Find all CrewedUsers by fullName
    List<CrewedUser> findByFullName(String fullName);

    // Find all CrewedUsers by position
    List<CrewedUser> findByPosition(String position);

    // Find all CrewedUsers by reportTime
    List<CrewedUser> findByReportTime(String reportTime);

    // combining search keys
    List<CrewedUser> findByGameIdAndPosition(Integer gameId, String position);


}
