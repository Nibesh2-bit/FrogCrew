package com.frogcrew.frogcrew_backend.Schedule.dto;

import jakarta.validation.constraints.NotEmpty;

public record GameDto(

    @NotEmpty(message = "gameId is required")
    Integer gameId,
    @NotEmpty(message = "ScheduleId is required")
    Integer scheduleId,
     String gameDate,
    String venue,
    String opponent,
    boolean isFinalized)

    {

    }
