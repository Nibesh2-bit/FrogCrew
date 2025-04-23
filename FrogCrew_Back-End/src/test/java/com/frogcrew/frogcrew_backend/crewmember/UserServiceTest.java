package com.frogcrew.frogcrew_backend.crewmember;

import com.frogcrew.frogcrew_backend.crewmember.dto.CrewMemberDto;
import com.frogcrew.frogcrew_backend.security.invite.EmailService;
import com.frogcrew.frogcrew_backend.security.invite.InvitationToken;
import com.frogcrew.frogcrew_backend.security.invite.InvitationRepository;
import com.frogcrew.frogcrew_backend.security.invite.dto.EmailDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private InvitationRepository invitationRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private CrewMemberDto validDto;

    @BeforeEach
    void setUp() {
        validDto = new CrewMemberDto(
                null,
                "John",
                "Doe",
                "john.doe@example.com",
                "1234567890",
                "P@ssw0rd",
                "ADMIN",
                List.of("Referee", "Coach")
        );
    }

    /**
     * Test that addUser saves the user when the token is valid and email matches.
     */
    @Test
    void testAddUserValidEmailAndToken() {
        InvitationToken validToken = new InvitationToken(
                "token123",
                validDto.email(),
                false,
                LocalDateTime.now().plusHours(1)
        );

        when(invitationRepository.findByToken("token123")).thenReturn(Optional.of(validToken));
        when(passwordEncoder.encode(validDto.password())).thenReturn("hashedPassword");


        CrewMemberUser savedUser = userService.addUser("token123", validDto);

        assertEquals(validDto.email(), savedUser.getEmail());
        assertEquals("hashedPassword", savedUser.getPassword());
        verify(userRepository).save(any(CrewMemberUser.class));
        verify(invitationRepository).save(validToken);
        assertTrue(validToken.isUsed());
    }

    /**
     * Test that addUser throws 404 when the token is not found.
     */
    @Test
    void testAddUserTokenNotFound() {
        when(invitationRepository.findByToken("badToken")).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                userService.addUser("badToken", validDto));

        assertEquals(404, ex.getStatusCode().value());
    }

    /**
     * Test that addUser throws 400 when the token is expired.
     */
    @Test
    void TestAddUserTokenIsExpired() {
        InvitationToken expiredToken = new InvitationToken(
                validDto.email(),
                "token123",
                false,
                LocalDateTime.now().minusMinutes(1)
        );

        when(invitationRepository.findByToken("token123")).thenReturn(Optional.of(expiredToken));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                userService.addUser("token123", validDto));

        assertEquals(400, ex.getStatusCode().value());
    }

    /**
     * Test that addUser throws 400 when the token email does not match DTO email.
     */
    @Test
    void testAddUserEmailDoesNotMatchToken() {
        InvitationToken mismatchedToken = new InvitationToken(
                "wrong.email@example.com",
                "token123",
                false,
                LocalDateTime.now().plusHours(1)
        );

        when(invitationRepository.findByToken("token123")).thenReturn(Optional.of(mismatchedToken));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                userService.addUser("token123", validDto));

        assertEquals(400, ex.getStatusCode().value());
    }

    /**
     * Test that validateToken returns true when the token is valid.
     */
    @Test
    void testValidateTokenSuccess() {
        InvitationToken validToken = new InvitationToken(
                validDto.email(),
                "token123",
                false,
                LocalDateTime.now().plusHours(1)
        );

        when(invitationRepository.findByToken("token123")).thenReturn(Optional.of(validToken));

        assertTrue(userService.validateToken("token123"));
    }

    /**
     * Test that validateToken returns false if the token is used or expired.
     */
    @Test
    void testValidateTokenWhenTokenIsUsedOrExpired() {
        InvitationToken usedToken = new InvitationToken(
                validDto.email(),
                "token123",
                true,
                LocalDateTime.now().plusHours(1)
        );
        when(invitationRepository.findByToken("token123")).thenReturn(Optional.of(usedToken));
        assertFalse(userService.validateToken("token123"));

        InvitationToken expiredToken = new InvitationToken(
                validDto.email(),
                "token123",
                false,
                LocalDateTime.now().minusMinutes(1)
        );
        when(invitationRepository.findByToken("token123")).thenReturn(Optional.of(expiredToken));
        assertFalse(userService.validateToken("token123"));
    }

    /**
     * Test that validateToken returns false if the token does not exist.
     */
    @Test
    void testValidateTokenWhenTokenNotFound() {
        when(invitationRepository.findByToken("unknownToken")).thenReturn(Optional.empty());
        assertFalse(userService.validateToken("unknownToken"));
    }

    /**
     * Test sendInvites sends an email for each address and stores the invitation.
     */
    @Test
    void testSendInvitesSuccess() {
        List<String> emails = List.of("test1@example.com", "test2@example.com");
        EmailDto emailDto = new EmailDto();
        emailDto.setEmails(emails);

        // Call the method
        userService.sendInvites(emailDto);

        // Capture how many invites were stored and emails sent
        verify(invitationRepository, times(2)).save(any(InvitationToken.class));
        verify(emailService, times(2)).send(anyString(), eq("FrogCrew Invite"), contains("Use this link to register:"));
    }
}
