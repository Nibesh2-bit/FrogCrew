package com.frogcrew.frogcrew_backend.Schedule;

import com.frogcrew.frogcrew_backend.system.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.endpoint.base-url}") // e.g., /api
public class GameScheduleController {
    private final GameScheduleService gameScheduleService;

    public GameScheduleController(GameScheduleService gameScheduleService) {
        this.gameScheduleService = gameScheduleService;
    }

    /**
     * Endpoint to retrieve all game schedule
     */

    @GetMapping("/gameSchedule/games")
    public Result findAllGames(){
        List<GameDTO> gameDTOs = gameScheduleService.
    }
}
