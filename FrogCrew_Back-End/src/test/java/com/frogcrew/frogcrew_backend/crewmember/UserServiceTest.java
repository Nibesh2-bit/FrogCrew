package com.frogcrew.frogcrew_backend.crewmember;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserService userService;

    List<CrewMemberUser> crewMemberUsers;
    @BeforeEach
    public void setUp() {

    }

    @Test
    void testSaveSuccess(){
        //Given

        CrewMemberUser newUser = new CrewMemberUser();
        newUser.setFirstName("John");
        newUser.setLastName("Doe");
        newUser.setEmail("john@doe.com");
        newUser.setPhoneNumber("password");
        newUser.setRole("ADMIN");
        List Positions = new ArrayList();
        Positions.add("Director");
        Positions.add("Producer");
        newUser.setPositions(Positions);
        //crewMemberUsers.add(newUser);

        given(this.passwordEncoder.encode(newUser.getPassword())).willReturn("password");
        given(this.userRepository.save(newUser)).willReturn(newUser);

        //When

        CrewMemberUser returnedUser = this.userService.save(newUser);

        //Then

        assertThat(returnedUser.getId()).isEqualTo(newUser.getId());
        assertThat(returnedUser.getFirstName()).isEqualTo(newUser.getFirstName());
        assertThat(returnedUser.getLastName()).isEqualTo(newUser.getLastName());
        assertThat(returnedUser.getEmail()).isEqualTo(newUser.getEmail());
        assertThat(returnedUser.getPhoneNumber()).isEqualTo(newUser.getPhoneNumber());
        assertThat(returnedUser.getRole()).isEqualTo(newUser.getRole());
        assertThat(returnedUser.getPositions()).isEqualTo(newUser.getPositions());
        verify(this.userRepository, times(1)).save(newUser);









    }








}
