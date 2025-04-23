package com.frogcrew.frogcrew_backend.crewmember;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frogcrew.frogcrew_backend.crewmember.dto.CrewMemberDto;
import com.frogcrew.frogcrew_backend.security.invite.InvitationRepository;
import com.frogcrew.frogcrew_backend.security.invite.InvitationToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for UserController.
 * These tests load the full application context and use MockMvc to simulate real HTTP requests.
 */
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private String token;

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    @BeforeEach
    void setUp() {
        // Create and save a valid invitation token before each test
        token = "123";
        InvitationToken invite = new InvitationToken("test@example.com", token, false, LocalDateTime.now().plusHours(1));
        invitationRepository.save(invite);
    }

    @Test
    void testValidateInviteSuccess() throws Exception {
        mockMvc.perform(get(this.baseUrl+"/invite/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("Invitation valid"));
    }

    @Test
    void testRegisterCrewMemberSuccess() throws Exception {
        CrewMemberDto dto = new CrewMemberDto(
                null,
                "Test",
                "User",
                "test@example.com",
                "1234567890",
                "TestPassword",
                "ADMIN",
                List.of("Coach")
        );

        mockMvc.perform(post(this.baseUrl+"/crewMember")
                        .param("token", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.data.email").value("test@example.com"));
    }

    @Test
    void testValidateInviteFailure() throws Exception {
        mockMvc.perform(get(this.baseUrl+"/invite/123"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.message").value("Invitation not valid"));
    }
}
