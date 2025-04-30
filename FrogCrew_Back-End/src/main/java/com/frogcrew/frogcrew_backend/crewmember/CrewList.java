package com.frogcrew.frogcrew_backend.crewmember;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Entity
public class CrewList implements Serializable{
     @Id  @GeneratedValue(strategy = GenerationType.AUTO)@NotNull(message = "GameId can not be empty.") Integer gameId;
     @NotNull(message = "GameStart can not be empty.") String gameStart;
     @NotNull(message = "GameDate can not be empty.") String gameDate;
     @NotNull(message = "Venue can not be empty.") String venue;
     @NotNull(message = "Opponent can not be empty.") String opponent;
     @NotNull(message = "CrewUsers can not be empty.")
     @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true) // Defines the relationship
     @JoinColumn(name = "crew_list_id")
     @NotNull(message = "CrewdUsers can not be empty.")
     List<CrewedUser> crewedUsers = new ArrayList<>();


     public @NotNull(message = "GameId can not be empty.") Integer getGameId() {
          return gameId;
     }

     public void setGameId(@NotNull(message = "GameId can not be empty.") Integer gameId) {
          this.gameId = gameId;
     }

     public @NotNull(message = "GameStart can not be empty.") String getGameStart() {
          return gameStart;
     }

     public void setGameStart(@NotNull(message = "GameStart can not be empty.") String gameStart) {
          this.gameStart = gameStart;
     }

     public @NotNull(message = "GameDate can not be empty.") String getGameDate() {
          return gameDate;
     }

     public void setGameDate(@NotNull(message = "GameDate can not be empty.") String gameDate) {
          this.gameDate = gameDate;
     }

     public @NotNull(message = "Venue can not be empty.") String getVenue() {
          return venue;
     }

     public void setVenue(@NotNull(message = "Venue can not be empty.") String venue) {
          this.venue = venue;
     }

     public @NotNull(message = "Opponent can not be empty.") String getOpponent() {
          return opponent;
     }

     public void setOpponent(@NotNull(message = "Opponent can not be empty.") String opponent) {
          this.opponent = opponent;
     }

     public @NotNull(message = "CrewUsers can not be empty.") @NotNull(message = "CrewdUsers can not be empty.") List<CrewedUser> getCrewedUsers() {
          return crewedUsers;
     }

     public void setCrewedUsers(@NotNull(message = "CrewUsers can not be empty.") @NotNull(message = "CrewdUsers can not be empty.") List<CrewedUser> crewedUsers) {
          this.crewedUsers = crewedUsers;
     }
}
