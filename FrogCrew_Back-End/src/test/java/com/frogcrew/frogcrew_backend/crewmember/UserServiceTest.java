package com.frogcrew.frogcrew_backend.crewmember;

import com.frogcrew.frogcrew_backend.crewmember.dto.CrewMemberDto;
import com.frogcrew.frogcrew_backend.crewmember.dto.SimpleUserDto;
import com.frogcrew.frogcrew_backend.invite.EmailService;
import com.frogcrew.frogcrew_backend.invite.InvitationService;
import com.frogcrew.frogcrew_backend.invite.InvitationToken;
import com.frogcrew.frogcrew_backend.invite.InvitationRepository;
import com.frogcrew.frogcrew_backend.invite.dto.EmailDto;
import com.frogcrew.frogcrew_backend.system.exception.ObjectNotFoundException;
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
    private SimpleUserDto validSimpleUserDto;
    private CrewMemberUser mockUser;
    private CrewMemberUser mockAdmin;

    @BeforeEach
    void setUp() {
        validDto = new CrewMemberDto(
                null,
                "John",
                "Doe",
                "john.doe@example.com",
                "1234567890",
                "P@ssw0rd",
                "Admin",
                List.of("Referee", "Coach")
        );
       mockUser = new CrewMemberUser();
        mockUser.setId(1); // Explicitly set an ID
        mockUser.setEmail("abc@tcu.edu");
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");
        mockUser.setPassword("<PASSWORD>");
        mockUser.setRole("User");
        mockUser.setPositions(List.of("Referee", "Coach"));
        mockUser.setPhoneNumber("1234567890");

        mockAdmin = new CrewMemberUser();

        mockAdmin.setId(1); // Explicitly set an ID
        mockAdmin.setEmail("abc@tcu.edu");
        mockAdmin.setFirstName("John");
        mockAdmin.setLastName("Doe");
        mockAdmin.setPassword("<PASSWORD>");
        mockAdmin.setRole("User");
        mockAdmin.setPositions(List.of("Referee", "Coach"));
        mockAdmin.setPhoneNumber("1234567890");

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
        // Arrange
        String token = "nonexistent-token"; // Simulates an invalid token
        CrewMemberDto dto = new CrewMemberDto(
                null,
                "test@example.com",     // Email
                "password123",          // Password
                "John",                 // First name
                "Doe",                  // Last name
                "User",                 // Role
                null,                   // Positions
                null                    // Phone Number (optional)
        );

        // Mock `invitationService.validateToken` to throw a `ResponseStatusException` for an invalid token
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
    public void testAddUserTokenIsExpired() {
        // Arrange: Mock the repository to return empty Optional for the token
        when(invitationRepository.findByToken("token123")).thenReturn(Optional.empty());

        // Act: Expect ResponseStatusException to be thrown
        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class, () -> userService.addUser("token123", validDto));

        // Assert: Verify exception details
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Token expired or already used", exception.getReason());

        // Verify: Ensure repository was queried exactly once
        verify(invitationRepository, times(1)).findByToken("token123");
    } /**
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

    @Test
    void testFindUserSuccess() {
        // Mock repository behavior

        when(userRepository.findById(mockUser.getId())).thenReturn(Optional.of(mockUser));

        // Call method under test
        CrewMemberUser result = userService.findById(mockUser.getId());

        // Assertions
        assertNotNull(result); // Ensure the result is not null
        assertEquals(mockUser.getId(), result.getId());
        assertEquals(mockUser.getEmail(), result.getEmail());

        // Verify repository interactions
        verify(userRepository, times(1)).findById(mockUser.getId());
    }

    @Test
    void testFindUserNotFound() {
        // Arrange: Mock repository behavior for a non-existent user ID
        Integer userId = mockUser.getId(); // Descriptive variable for mock user ID
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert: Expect ObjectNotFoundException
        ObjectNotFoundException notFoundException = assertThrows(
                ObjectNotFoundException.class,
                () -> userService.findById(userId), "Can not find user with Id: " + userId + " :("
        );


        // Assert: Verify exception message content
        String errorMessage = notFoundException.getMessage();
        assertTrue(errorMessage.contains("user"), "Exception message does not mention 'user'");
        assertTrue(errorMessage.contains(userId.toString()), "Exception message does not include user Id: " + userId);

        // Verify: Ensure repository interaction is correct
        verify(userRepository, times(1)).findById(userId);
    }


    @Test
    void testFindUserAdminViewSuccess() {
        // Mock repository behavior for admin user
        mockAdmin.setRole("Admin");
        when(userRepository.findById(mockAdmin.getId())).thenReturn(Optional.of(mockAdmin));

        // Call method under test
        CrewMemberUser result = userService.getCrewMemberAdminView(mockAdmin.getId());

        // Assertions
        assertNotNull(result);
        assertEquals(mockAdmin.getId(), result.getId());
        assertEquals(mockAdmin.getRole(), "Admin");

        // Verify repository interaction
        verify(userRepository, times(1)).findById(mockAdmin.getId());
    }

    @Test
    void testFindUserAdminViewNotFound() {
        // Mock repository behavior for a non-existent user
        when(userRepository.findById(mockAdmin.getId())).thenReturn(Optional.empty());

        // Assertions and exception handling
        ObjectNotFoundException exception = assertThrows(
                ObjectNotFoundException.class,
                () -> userService.getCrewMemberAdminView(mockAdmin.getId()),
                "Could not find user with Id: " + mockAdmin.getId() + " :("
        );

        assertTrue(exception.getMessage().contains("user"));
        assertTrue(exception.getMessage().contains(mockAdmin.getId().toString()));

        // Verify repository interaction
        verify(userRepository, times(1)).findById(mockAdmin.getId());
    }
}