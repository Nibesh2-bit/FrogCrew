package com.frogcrew.frogcrew_backend.Schedule;

import com.frogcrew.frogcrew_backend.Schedule.dto.GameDto;
import com.frogcrew.frogcrew_backend.system.Result;
import com.frogcrew.frogcrew_backend.system.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("${api.endpoint.base-url}") // Configurable base URL, e.g., /api
public class GameController {

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * Retrieve the general game schedule with optional sorting and filtering.
     * Endpoint: GET /gameSchedule?sortBy={field}&filterField={value}
     */
    @PreAuthorize("hasAuthority('ROLE_CrewMember')") // Ensure only Crew Members can access this
    @GetMapping("/gameSchedule")
    public Result viewGeneralGameSchedule(
            @RequestParam(value = "sortBy", defaultValue = "date") String sortBy,
            @RequestParam(value = "filterDate", required = false) String filterDate,
            @RequestParam(value = "filterVenue", required = false) String filterVenue,
            @RequestParam(value = "filterOpponent", required = false) String filterOpponent) {
        try {
            List<GameDto> schedule;

            // Handle filtering and sorting
            if (filterDate != null || filterVenue != null || filterOpponent != null) {
                schedule = gameService.filterGamesByCriteria(
                        filterDate != null ? LocalDate.parse(filterDate) : null,
                        null, // Add endDate logic if needed
                        filterVenue,
                        filterOpponent
                );
            } else {
                schedule = gameService.viewSortedSchedule(sortBy);
            }

            // Check if the schedule is empty (Extension 3a)
            if (schedule.isEmpty()) {
                return new Result(false, StatusCode.NOT_FOUND, "No upcoming games available at this time.", null);
            }

            return new Result(true, StatusCode.SUCCESS, "Schedule retrieved successfully.", schedule);
        } catch (Exception e) {
            return new Result(false, StatusCode.ERROR, "An error occurred.", null);
        }
    }

    /**
     * Retrieve the details of a specific game by its ID.
     * Endpoint: GET /gameSchedule/{gameId}
     */
    @PreAuthorize("hasAuthority('ROLE_CrewMember')")
    @GetMapping("/gameSchedule/{gameId}")
    public Result viewGameDetails(@PathVariable Integer gameId) {
        try {
            Game gameDetails = gameService.viewGameById(gameId);
            return new Result(true, StatusCode.SUCCESS, "Game details retrieved successfully.", gameDetails);
        } catch (Exception e) {
           return new Result(false, StatusCode.NOT_FOUND, "Game not found.", null);
        }
    }
}