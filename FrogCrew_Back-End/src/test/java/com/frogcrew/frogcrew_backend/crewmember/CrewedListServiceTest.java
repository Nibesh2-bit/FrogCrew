package com.frogcrew.frogcrew_backend.crewmember;

import com.frogcrew.frogcrew_backend.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith( MockitoExtension.class)
public class CrewedListServiceTest {

    @InjectMocks
    CrewListService crewListService;

    @Mock
    CrewListRepository crewListRepository;

    @BeforeEach
    void setUp() {

        CrewList a = new CrewList();
        a.setGameId(1);
        a.setGameStart("2022-01-01");
        a.setGameDate("2022-01-01");
        a.setVenue("Venue");
        a.setOpponent("Opponent");

        CrewList b = new CrewList();
        b.setGameId(2);
        b.setGameStart("2022-01-02");
        b.setGameDate("2022-01-02");
        b.setVenue("Venue");
        b.setOpponent("Opponent");

        CrewList c = new CrewList();
        c.setGameId(3);
        c.setGameStart("2022-01-03");
        c.setGameDate("2022-01-03");
        c.setVenue("Venue");
        c.setOpponent("Opponent");

        CrewList d = new CrewList();
        d.setGameId(4);
        d.setGameStart("2022-01-04");
        d.setGameDate("2022-01-04");
        d.setVenue("Venue");
        d.setOpponent("Opponent");



    }

    @Test
    void testFindAllCrewListSuccess() {
        given(this.crewListRepository.findAll()).willReturn(new ArrayList<>());

        List<CrewList> crewLists = this.crewListRepository.findAll();
        assertThat(crewLists.size()).isEqualTo(0);

        verify(this.crewListRepository, times(1)).findAll();
    }

    @Test
    void testFindByIdSuccess() {
        CrewList a = new CrewList();
        a.setGameId(1);
        a.setGameStart("2022-01-01");
        a.setGameDate("2022-01-01");
        a.setVenue("Venue");
        a.setOpponent("Opponent");

        given(this.crewListRepository.findById(1)).willReturn(Optional.of(a));
        CrewList returnedCrewList = this.crewListService.findById(1);
        assertThat(returnedCrewList).isEqualTo(a);
        assertThat(returnedCrewList.getGameId()).isEqualTo(1);
        assertThat(returnedCrewList.getGameStart()).isEqualTo("2022-01-01");
        assertThat(returnedCrewList.getGameDate()).isEqualTo("2022-01-01");
        assertThat(returnedCrewList.getVenue()).isEqualTo("Venue");
        assertThat(returnedCrewList.getOpponent()).isEqualTo("Opponent");
        verify(this.crewListRepository, times(1)).findById(1);
    }
    @Test void testFindByIdNotFound() {
        given(this.crewListRepository.findById(1)).willReturn(Optional.empty());

        Throwable thrown = catchThrowable(() ->{CrewList returnedCrewList = crewListRepository.findById(1).get();});
        assertThat(thrown).isInstanceOf(BadCredentialsException.class)
                .hasMessage("Could not find CrewList with Id 1 :(");
        verify(this.crewListRepository, times(1)).findById(1);

    }
    @Test
    void testFindByGameIdSuccess() {
        // Arrange
        CrewList a = new CrewList();
        a.setGameId(1);
        a.setGameStart("2022-01-01");
        a.setGameDate("2022-01-01");
        a.setVenue("Venue");
        a.setOpponent("Opponent");


        given(this.crewListRepository.findByGameId(1)).willReturn(a);

        // Act
        CrewList crewList = this.crewListService.findByGameId(1);

        // Assert
        assertThat(crewList).isNotNull(); // Ensure the returned object is not null
        assertThat(crewList).isEqualTo(a); // Ensure the returned object matches the mock
        assertThat(crewList.getGameId()).isEqualTo(1);
        assertThat(crewList.getGameStart()).isEqualTo("2022-01-01");
        assertThat(crewList.getGameDate()).isEqualTo("2022-01-01");
        assertThat(crewList.getVenue()).isEqualTo("Venue");
        assertThat(crewList.getOpponent()).isEqualTo("Opponent");
        assertThat(crewList.getCrewedUsers()).isEmpty(); // Additional assertion for crewedUsers

        verify(this.crewListRepository, times(1)).findByGameId(1);
    }
    @Test
    void testFindByGameIdNotFound() {
        final int gameId = 1;

        // Arrange
        given(this.crewListRepository.findByGameId(gameId)).willReturn(Optional.empty());

        // Act
        Throwable thrown = catchThrowable(() -> this.crewListService.findByGameId(gameId));

        // Assert
        assertThat(thrown).isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find CrewList with Id " + gameId + " :(");
        verify(this.crewListRepository, times(1)).findByGameId(gameId);
    }






}
