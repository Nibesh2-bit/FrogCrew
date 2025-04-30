package com.frogcrew.frogcrew_backend.Schedule.dto;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;

public record GameScheduleDTO(@Id @NotEmpty(message = "ScheduleId is required") Integer id, Integer sport, Integer season) {

}
