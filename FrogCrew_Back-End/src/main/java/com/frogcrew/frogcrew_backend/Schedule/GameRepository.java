package com.frogcrew.frogcrew_backend.Schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {
    @Query("SELECT g FROM Game g ORDER BY g.date ASC")
    List<Game> findByOrderByDateAsc(); // Retrieve games sorted by date (ascending)


    @Query("SELECT g FROM Game g ORDER BY LOWER(g.opponent) ASC")
    List<Game> findByOrderByOpponentAsc();

    @Query("SELECT g FROM Game g ORDER BY LOWER(g.venue) ASC")
    List<Game> findByOrderByVenueAsc();

    List<Game> findGamesByDateBetweenAndVenueAndOpponent(LocalDate startDate, LocalDate endDate, String venue, String opponent);
}
