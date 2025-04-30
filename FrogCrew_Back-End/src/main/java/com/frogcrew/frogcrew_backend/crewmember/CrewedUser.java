package com.frogcrew.frogcrew_backend.crewmember;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;

@Entity
public class CrewedUser implements Serializable{

        @Id @NotEmpty(message = "UserId can not be empty.")  Integer userId;
        @NotEmpty(message = "GameId can not be empty.")  Integer gameId;
        @NotEmpty(message = "FullName can not be empty." ) String fullName;
        @NotEmpty(message = "Position can not be empty.") String position;
        @NotEmpty(message = "ReportTime can not be empty.") String reportTime;



}
