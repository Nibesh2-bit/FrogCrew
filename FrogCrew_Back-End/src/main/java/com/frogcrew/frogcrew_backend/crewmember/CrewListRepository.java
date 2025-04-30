package com.frogcrew.frogcrew_backend.crewmember;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CrewListRepository extends JpaRepository<CrewList, Integer>{

    CrewList findByGameId(Integer gameId);

    // Find all by gameStart
    List<CrewList> findByGameStart(String gameStart);

    // Find all by gameDate
    List<CrewList> findByGameDate(String gameDate);

    // Find all by venue
    List<CrewList> findByVenue(String venue);

    // Find all by opponent
    List<CrewList> findByOpponent(String opponent);



    // Find all where CrewedUser list is not empty
    List<CrewList> findByCrewUsersIsNotEmpty();

}
