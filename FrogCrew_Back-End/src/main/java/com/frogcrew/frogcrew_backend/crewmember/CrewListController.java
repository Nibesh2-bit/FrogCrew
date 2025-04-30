package com.frogcrew.frogcrew_backend.crewmember;


import com.frogcrew.frogcrew_backend.system.Result;
import com.frogcrew.frogcrew_backend.system.StatusCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.endpoint.base-url}")public class CrewListController {

    private final CrewListService crewListService;

    public CrewListController(CrewListService crewListService) {
        this.crewListService = crewListService;
    }

    @GetMapping("/crewSchedule/{gameId}")
    public Result findAllCrewListsByGameId(@PathVariable Integer gameId) {
      CrewList foundLists = this.crewListService.findByGameId(gameId);

        return new Result(true, StatusCode.SUCCESS, "Find all crew lists by game id", foundLists);
    }


    @GetMapping("/crewSchedule")
    public Result findAllCrewLists() {
        List<CrewList> foundLists = this.crewListService.findAll();

        return new Result(true, StatusCode.SUCCESS, "Find all crew lists", foundLists);
    }

    @GetMapping("/crewSchedule/{venue}")
    public Result findAllCrewListsByVenue(@PathVariable String venue) {
        List<CrewList> foundLists = this.crewListService.findByGameVenue(venue);
        return new Result(true, StatusCode.SUCCESS, "Find all crew lists by venue", foundLists);
    }

    @GetMapping("/crewSchedule/{opponent}")
    public Result findAllCrewListsByOpponent(@PathVariable String opponent) {
        List<CrewList> foundLists = this.crewListService.findByGameOpponent(opponent);
        return new Result(true, StatusCode.SUCCESS, "Find all crew lists by opponent", foundLists);
    }

    @GetMapping("/crewSchedule/{gameStart}")
    public Result findAllCrewListsByGameStart(@PathVariable String gameStart) {
        List<CrewList> foundLists = this.crewListService.findByGameStart(gameStart);
        return new Result(true, StatusCode.SUCCESS, "Find all crew lists by game start", foundLists);
    }
    @GetMapping("/crewSchedule/{gameDate}")
    public Result findAllCrewListsByGameDate(@PathVariable String gameDate) {
        List<CrewList> foundLists = this.crewListService.findByGameDate(gameDate);
        return new Result(true, StatusCode.SUCCESS, "Find all crew lists by game date", foundLists);
    }




}
