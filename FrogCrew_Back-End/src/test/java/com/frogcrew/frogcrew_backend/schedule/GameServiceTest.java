package com.frogcrew.frogcrew_backend.schedule;

import com.frogcrew.frogcrew_backend.Schedule.Game;
import com.frogcrew.frogcrew_backend.Schedule.GameRepository;
import com.frogcrew.frogcrew_backend.Schedule.GameService;
import com.frogcrew.frogcrew_backend.Schedule.dto.GameDto;
import com.frogcrew.frogcrew_backend.system.exception.ObjectNotFoundException;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.InstanceOfAssertFactories.LOCAL_DATE;
import static org.assertj.core.api.InstanceOfAssertFactories.OPTIONAL;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {
    @Mock
    GameRepository gameRepository;
    @InjectMocks
    GameService gameService;

    List<Game> preDefinedGames ;

    @BeforeEach
    void setUp() {
        Game a = new Game();
        a.setDate(LocalDate.of(2022, 1, 1));
        a.setOpponent("Texas");
        a.setVenue("Schollmaier");
        Game b = new Game();
        b.setDate(LocalDate.of(2022, 1, 2));
        b.setOpponent("A&M");
        b.setVenue("Kyle");
        Game c = new Game();
        c.setDate(LocalDate.of(2022, 1, 3));
        c.setOpponent("SMU");
        c.setVenue("Schollmaier");

        this.preDefinedGames = new ArrayList<>();
        this.preDefinedGames.add(a);
        this.preDefinedGames.add(b);
        this.preDefinedGames.add(c);
        gameRepository.save(a);

        gameRepository.save(b);
        gameRepository.save(c);

    }




    @Test void testViewGameScheduleSuccess() {

        given(this.gameRepository.findByOrderByDateAsc()).willReturn(this.preDefinedGames);
         List<GameDto> games = this.gameService.viewGameSchedule();

         assertThat(games.size()).isEqualTo(this.preDefinedGames.size());

       verify(this.gameRepository, times(1)).findByOrderByDateAsc();

}
@Test void testViewGameScheduleEmptyList() {
        given(this.gameRepository.findByOrderByDateAsc()).willReturn(new ArrayList<>());

        List<GameDto> games = this.gameService.viewGameSchedule();

        assertThat(games.size()).isEqualTo(0);

        verify(this.gameRepository, times(1)).findByOrderByDateAsc();

}

@Test void testViewGameScheduleNullList() {
        given(this.gameRepository.findByOrderByDateAsc()).willReturn(null);

    List<GameDto> games = this.gameService.viewGameSchedule();

    assertThat(games.size()).isEqualTo(0);

    verify(this.gameRepository, times(1)).findByOrderByDateAsc();
}

@Test void testViewGameByIDSuccess() {
        Game a = this.preDefinedGames.get(0);
        given(this.gameRepository.findById(a.getId())).willReturn(java.util.Optional.of(a));
        Game returnedGame = gameService.viewGameById(a.getId());
        assertThat(returnedGame).isEqualTo(a);
        verify(this.gameRepository, times(1)).findById(a.getId());
}

@Test void testViewGameByIDNotFound() {
       //given
    given(this.gameRepository.findById(100)).willReturn(java.util.Optional.empty());
       //when
    Throwable thrown = catchThrowable(() ->{Game returnedGame = gameService.viewGameById(100);});

    //Then

    assertThat(thrown).isInstanceOf(ObjectNotFoundException.class)
            .hasMessage("Could not find Game with Id 100 :(");
    verify(this.gameRepository, times(1)).findById(100);



}
@Test void testViewSortedGameScheduleByDateSuccess() {
    given(this.gameRepository.findByOrderByDateAsc()).willReturn(this.preDefinedGames);
    List<GameDto> sortByDate = this.gameService.viewSortedSchedule("date");

    assertThat(sortByDate.get(0).gameDate()).isEqualTo(LocalDate.of(2022, 1, 1).toString());
    assertThat(sortByDate.get(1).gameDate()).isEqualTo(LocalDate.of(2022, 1, 2).toString());
    assertThat(sortByDate.get(2).gameDate()).isEqualTo(LocalDate.of(2022, 1, 3).toString());

    verify(this.gameRepository, times(1)).findByOrderByDateAsc();

}
@Test void testViewSortedGameScheduleByOpponentSuccess() {
        given(this.gameRepository.findByOrderByOpponentAsc()).willReturn(this.preDefinedGames);
        List<GameDto> sortByOpponent = this.gameService.viewSortedSchedule("opponent");
        assertThat(sortByOpponent.get(0).opponent()).isEqualTo("A&M");
        assertThat(sortByOpponent.get(1).opponent()).isEqualTo("SMU");
        assertThat(sortByOpponent.get(2).opponent()).isEqualTo("Texas");
        verify(this.gameRepository, times(1)).findByOrderByOpponentAsc();




}





}
