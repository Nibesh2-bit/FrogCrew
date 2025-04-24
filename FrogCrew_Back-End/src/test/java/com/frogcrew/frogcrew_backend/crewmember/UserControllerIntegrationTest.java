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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for UserController.
 * These tests load the full application context and use MockMvc to simulate real HTTP requests.
 */

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Integration tests for User API endpoints")
@Tag("integration")
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private String email_token;

    private String token;

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    @BeforeEach
    void setUp() throws Exception{
        // Create and save a valid invitation email_token before each test
        email_token = "123";
        InvitationToken invite = new InvitationToken("test@example.com", email_token, false, LocalDateTime.now().plusHours(1));
        invitationRepository.save(invite);
        ResultActions resultActions = this.mockMvc.perform(post(this.baseUrl + "/auth/login").with(httpBasic("john", "123456"))); // httpBasic() is from spring-security-test.
        MvcResult mvcResult = resultActions.andDo(print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject json = new JSONObject(contentAsString);
        this.token = "Bearer " + json.getJSONObject("data").getString("token");

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
                        .param("email_token", email_token)
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


    /**
     * api doc tests
     */
    @Test
    @DisplayName("Check find all users (GET)")
    void testFindAllUserSuccess() throws Exception {
        mockMvc.perform(get(this.baseUrl+"/users").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(3)));
    }









}
