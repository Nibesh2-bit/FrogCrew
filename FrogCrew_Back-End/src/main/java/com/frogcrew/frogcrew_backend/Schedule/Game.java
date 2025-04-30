package com.frogcrew.frogcrew_backend.Schedule;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id") // FK to Schedule table
    private GameSchedule schedule;

    private LocalDate date;
    private LocalTime time;
    private String opponent;
    private String venue;
    private String Season ;

    public void setSchedule(GameSchedule schedule) {
        this.schedule = schedule;
    }

    public void setSeason(String season) {
        Season = season;
    }

    public boolean isFinalized() {
        return isFinalized;
    }

    public void setFinalized(boolean finalized) {
        isFinalized = finalized;
    }

    private  boolean isFinalized;

    @ElementCollection
    private List<String> requiredCrewPositions; // e.g., ["Technician", "Event Manager"]

    // Getters and Setters omitted for brevity


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getOpponent() {
        return opponent;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public List<String> getRequiredCrewPositions() {
        return requiredCrewPositions;
    }

    public void setRequiredCrewPositions(List<String> requiredCrewPositions) {
        this.requiredCrewPositions = requiredCrewPositions;
    }

    public GameSchedule getSchedule() {
        return this.schedule;
    }

    public String getSeason() {
        return Season;
    }
}