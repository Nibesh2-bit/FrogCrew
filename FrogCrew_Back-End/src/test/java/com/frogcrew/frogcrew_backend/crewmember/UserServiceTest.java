package com.frogcrew.frogcrew_backend.crewmember;

import com.frogcrew.frogcrew_backend.crewmember.dto.CrewMemberDto;
import com.frogcrew.frogcrew_backend.crewmember.invite.EmailService;
import com.frogcrew.frogcrew_backend.crewmember.invite.InvitationService;
import com.frogcrew.frogcrew_backend.crewmember.invite.InvitationToken;
import com.frogcrew.frogcrew_backend.crewmember.invite.InvitationRepository;
import com.frogcrew.frogcrew_backend.crewmember.invite.dto.EmailDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
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
    private InvitationService invitationService;

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

        when(invitationService.validateToken("token123",validDto.email())).thenReturn(validToken);
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
// // when(invitationRepository.findByToken("badToken","" ).thenReturn(Optional.empty()));
//        InvitationToken invalidToken = new InvitationToken(
//                "badToken",
//                validDto.email(),
//                false,
//                LocalDateTime.now().plusHours(1)
//        );
//
//
//        when(invitationService.validateToken("badToken", validDto.email())).thenReturn(invalidToken);
//
//        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
//                userService.addUser("badToken", validDto));
//
////        assertEquals(404, ex.getStatusCode().value());

            // Arrange
            String token = "nonexistent-token"; // Simulating an invalid token
            CrewMemberDto dto = new CrewMemberDto(
                    null,
                    "test@example.com",  // Email
                    "password123",       // Password
                    "John",              // First name
                    "Doe",               // Last name
                    "USER",              // Role
                    null,                // Positions
                    null                 // Phone Number (optional in this case)
            );

            // Mock `invitationService.validateToken` to throw a `ResponseStatusException` for the invalid token
            when(invitationService.validateToken(eq(token), eq(dto.email())))
                    .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Invitation not valid"));

            // Act & Assert
            ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
                userService.addUser(token, dto);
            });

            // Assertions
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());  // Check the status code
            assertTrue(exception.getReason().contains("Invitation not valid")); // Check the reason message



    }

    /**
     * Test that addUser throws 400 when the token is expired.
     */
    @Test
    void TestAddUserTokenIsExpired() {// Arrange
//        InvitationToken expiredToken = new InvitationToken(
//                "token123",
//                validDto.email(),
//                false,
//                LocalDateTime.now().minusMinutes(1) // Token has expired
//        );
//
//        // Mocking the `invitationService.validateToken` method to return an expired token
//        when(invitationService.validateToken("token123", validDto.email())).thenReturn(expiredToken);
//
//
//        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
//                userService.addUser("token123", validDto)); // Call to the method under test
//
//        // Verify the thrown exception asserts match the expectations
//        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode()); // Expecting 400 Bad Request
//        assertEquals("Token is expired", ex.getReason()); // Validation for error reason (if applicable)
        // Arrange
        // Create an expired token
        InvitationToken expiredToken = new InvitationToken(
                "token123",
                validDto.email(),
                false, // Not used
                LocalDateTime.now().minusMinutes(1) // Expired timestamp
        );

        // Mock the repository to return the expired token
        when(invitationRepository.findByToken("token123"))
                .thenReturn(Optional.of(expiredToken));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                invitationService.validateToken("token123", validDto.email()) // Directly invoke validateToken
        );

        // Validate exception code and reason
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Token expired or already used", exception.getReason());

        // (Optional) Verify the repository interaction
        verify(invitationRepository).findByToken("token123");
    }

    /**
     * Test that addUser throws 400 when the token email does not match DTO email.
     */
    @Test
    void testAddUserEmailDoesNotMatchToken() {
//        InvitationToken mismatchedToken = new InvitationToken(
//
//                "token123",
//                "wrong.email@example.com",
//                false,
//                LocalDateTime.now().plusHours(1)
//        );
//
////        when(invitationRepository.findByToken("token123")).thenReturn(Optional.of(mismatchedToken));
//        when(invitationService.validateToken("token123", "wrong.email@example.com")).thenReturn(mismatchedToken);
//
//
//        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
//                userService.addUser("token123", validDto));
//
//        assertEquals(400, ex.getStatusCode().value());
        // Arrange
        String token = "valid-token";
        CrewMemberDto dto = new CrewMemberDto(null,
                "user",

                "test",
                "wrong@example.com",

                "6823049327",
                "password",
                null,
                null
        );

        when(invitationService.validateToken(eq(token), eq(dto.email())))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email does not match invitation"));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userService.addUser(token, dto);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Email does not match invitation"));
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