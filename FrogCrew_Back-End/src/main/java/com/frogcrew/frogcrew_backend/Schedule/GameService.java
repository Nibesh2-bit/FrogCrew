package com.frogcrew.frogcrew_backend.Schedule;

import com.frogcrew.frogcrew_backend.Schedule.dto.GameDto;
import com.frogcrew.frogcrew_backend.system.StatusCode;
import com.frogcrew.frogcrew_backend.system.exception.ObjectNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
@Service
@Transactional
public class GameService {

    @Autowired
    private GameRepository gameRepository;
    // Retrieves all games and maps them to GameDto
    @PreAuthorize("hasAuthority('ROLE_User')")
    public List<GameDto> viewGameSchedule() {
        List<Game> games = gameRepository.findByOrderByDateAsc(); // Only retrieve published games
        if (games == null || games.isEmpty()) {
            // Instead of throwing an exception, return an empty list
            return Collections.emptyList();
        }
        return games.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Filters no available games and converts to appropriate message
    public List<GameDto> handleNoGames() {
        if (gameRepository.count() == 0) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.NOT_FOUND,
                    "No games available");
        }
        return viewGameSchedule();
    }
    public Game viewGameById(Integer id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Game" , id));
        return game;
    }


    public List<GameDto> viewSortedSchedule(String sortBy) {
        List<Game> games;
        switch (sortBy.toLowerCase()) {
            case "opponent":
                games = gameRepository.findByOrderByOpponentAsc();
                break;
            case "venue":
                games = gameRepository.findByOrderByVenueAsc();
                break;
            default:
                games = gameRepository.findByOrderByDateAsc(); // Default sorting by date
        }
        return games.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    public List<GameDto> filterGamesByCriteria(LocalDate startDate, LocalDate endDate, String venue, String opponent) {
        List<Game> games = gameRepository.findGamesByDateBetweenAndVenueAndOpponent(
                startDate, endDate, venue, opponent);
        return games.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    private GameDto convertToDto(Game game) {
        Integer scheduleId = (game.getSchedule() != null) ? game.getSchedule().getId() : null;

        return new GameDto(
                game.getId(),
                scheduleId,
                game.getDate().toString(),
                game.getVenue(),
                game.getOpponent(),
                game.isFinalized()
        );
    }
}
