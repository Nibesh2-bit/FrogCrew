package com.frogcrew.frogcrew_backend.crewmember;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class CrewList implements Serializable {

     @Id
     @GeneratedValue(strategy = GenerationType.AUTO) // Primary key, auto-generated
     private Integer id;

     @NotNull(message = "GameId cannot be empty.")
     @Column(unique = true, nullable = false) // Enforce uniqueness for gameId (business key)
     private Integer gameId;

     @NotNull(message = "GameStart cannot be empty.")
     private String gameStart;

     @NotNull(message = "GameDate cannot be empty.")
     private String gameDate;

     @NotNull(message = "Venue cannot be empty.")
     private String venue;

     @NotNull(message = "Opponent cannot be empty.")
     private String opponent;

     @OneToMany(mappedBy = "crewList", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
     @NotNull(message = "CrewUsers cannot be null.")
     @Size(min = 1, message = "CrewUsers list cannot be empty.")
     private List<CrewedUser> crewedUsers = new ArrayList<>();

     // Default constructor (required by JPA)
     public CrewList() {}

     // Getters and Setters
     public Integer getId() {
          return id;
     }

     public void setId(Integer id) {
          this.id = id;
     }

     public Integer getGameId() {
          return gameId;
     }

     public void setGameId(Integer gameId) {
          this.gameId = gameId;
     }

     public String getGameStart() {
          return gameStart;
     }

     public void setGameStart(String gameStart) {
          this.gameStart = gameStart;
     }

     public String getGameDate() {
          return gameDate;
     }

     public void setGameDate(String gameDate) {
          this.gameDate = gameDate;
     }

     public String getVenue() {
          return venue;
     }

     public void setVenue(String venue) {
          this.venue = venue;
     }

     public String getOpponent() {
          return opponent;
     }

     public void setOpponent(String opponent) {
          this.opponent = opponent;
     }

     public List<CrewedUser> getCrewedUsers() {
          return crewedUsers;
     }

     public void setCrewedUsers(List<CrewedUser> crewedUsers) {
          this.crewedUsers = crewedUsers;
     }
}