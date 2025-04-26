package com.frogcrew.frogcrew_backend.crewmember;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frogcrew.frogcrew_backend.crewmember.dto.CrewMemberDto;
import com.frogcrew.frogcrew_backend.crewmember.invite.InvitationRepository;
import com.frogcrew.frogcrew_backend.crewmember.invite.InvitationToken;

import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Integration tests for User API endpoints")
@Tag("integration")
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private String email_token;

    private String token;

    @Autowired
    private UserRepository userRepository;

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    @BeforeEach
    void setUp() throws Exception {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // Clean up database to ensure a clean state
        userRepository.deleteAll();
        invitationRepository.deleteAll();

        // Preload database with a valid CrewMemberUser
        CrewMemberUser testUser = new CrewMemberUser();
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setEmail("john@example.com");
        testUser.setPassword(passwordEncoder.encode("123456"));
        testUser.setPhoneNumber("1234567890");
        testUser.setRole("ROLE_USER");
        testUser.setPositions(List.of("Engineer"));
        userRepository.save(testUser);

        // Preload an invitation token
        email_token = "123";
        InvitationToken invite = new InvitationToken( email_token, "test@example.com",false, LocalDateTime.now().plusHours(1));
        invitationRepository.save(invite);

        // Perform login with preloaded credentials
        ResultActions resultActions = this.mockMvc.perform(post(this.baseUrl + "/auth/login")
                .with(httpBasic("john@example.com", "123456")));
        MvcResult mvcResult = resultActions.andDo(print()).andReturn();

        int status = mvcResult.getResponse().getStatus();
        if (status == 200) {
            String contentAsString = mvcResult.getResponse().getContentAsString();
            JSONObject json = new JSONObject(contentAsString);
            this.token = "Bearer " + json.getJSONObject("data").getString("token");
        } else {
            throw new IllegalStateException("Login failed during test setup. Please check credentials or endpoint.");
        }
    }

    @Test
    @DisplayName("Validate Invite: Success")
    void testValidateInviteSuccess() throws Exception {
        mockMvc.perform(get(this.baseUrl + "/invite/" + email_token)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("Invitation valid"));
    }

    @Test
    @DisplayName("Register Crew Member: Success")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testRegisterCrewMemberSuccess() throws Exception {

        CrewMemberUser user = new CrewMemberUser();
        user.setFirstName("Regular");
        user.setLastName("CrewMember");
        user.setEmail("regular@example.com");
        user.setPhoneNumber("0987654321");
        user.setPassword("RegularPassword");
        user.setRole("USER");
        user.setPositions(List.of("Director", "Producer"));

        mockMvc.perform(post(this.baseUrl + "/crewMember")
                        .param("email_token", email_token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.data.email").value("regular@example.com"));
    }

    @Test
    @DisplayName("Validate Invite: Failure")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testValidateInviteFailure() throws Exception {
        // Use a nonexistent invitation token
        mockMvc.perform(get(this.baseUrl + "/invite/" + "invalid_token")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.message").value("Invitation not valid"));
    }

    @Test
    @DisplayName("Find All Users: Success")
    void testFindAllUsersSuccess() throws Exception {
        mockMvc.perform(get(this.baseUrl + "/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(1))); // Assuming only one user is preloaded
    }
}