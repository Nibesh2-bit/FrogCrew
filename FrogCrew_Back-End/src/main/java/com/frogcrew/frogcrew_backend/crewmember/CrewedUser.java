package com.frogcrew.frogcrew_backend.crewmember;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;

@Entity
public class CrewedUser implements Serializable {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO) // Primary key, auto-generated
        private Integer userId;

        @NotNull(message = "GameId cannot be empty.")
        private Integer gameId; // Foreign key mapping to CrewList's gameId

        @NotNull(message = "FullName cannot be empty.")
        private String fullName;

        @NotNull(message = "Position cannot be empty.")
        private String position;

        @NotNull(message = "ReportTime cannot be empty.")
        private String reportTime;

        @ManyToOne
        @JoinColumn(name = "gameId", referencedColumnName = "gameId", insertable = false, updatable = false)
        private CrewList crewList;

        // Default constructor (required by JPA)
        public CrewedUser() {}

        // Getters and Setters
        public Integer getUserId() {
                return userId;
        }

        public void setUserId(Integer userId) {
                this.userId = userId;
        }

        public Integer getGameId() {
                return gameId;
        }

        public void setGameId(Integer gameId) {
                this.gameId = gameId;
        }

        public String getFullName() {
                return fullName;
        }

        public void setFullName(String fullName) {
                this.fullName = fullName;
        }

        public String getPosition() {
                return position;
        }

        public void setPosition(String position) {
                this.position = position;
        }

        public String getReportTime() {
                return reportTime;
        }

        public void setReportTime(String reportTime) {
                this.reportTime = reportTime;
        }

        public CrewList getCrewList() {
                return crewList;
        }

        public void setCrewList(CrewList crewList) {
                this.crewList = crewList;
        }
}