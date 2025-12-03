package com.vitalioleksenko.csp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitalioleksenko.csp.config.SecurityConfig;
import com.vitalioleksenko.csp.security.JwtFilter;
import com.vitalioleksenko.csp.services.MembershipsService; // припущення
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(MembershipsController.class)
@WebMvcTest(controllers = MembershipsController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                SecurityConfig.class,
                JwtFilter.class
        })
})
@AutoConfigureMockMvc(addFilters = false)
class MembershipsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @TestConfiguration
    static class TestConfig {
        @Bean
        MembershipsService membershipsService() {
            return Mockito.mock(MembershipsService.class);
        }
    }

    @Autowired
    MembershipsService membershipsService;

    // ---------- POST /api/membership ----------
    @Test
    void createMembership_ok() throws Exception {
        String body = """
            {
              "userId": 1,
              "groupId": 2,
              "role": "MEMBER"
            }
            """;

        mockMvc.perform(post("/api/membership")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());
    }

    // ---------- PATCH /api/membership/{id} ----------
    @Test
    void updateMembership_ok() throws Exception {
        String body = """
            {
              "role": "TEAM_LEAD"
            }
            """;

        mockMvc.perform(patch("/api/membership/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());
    }

    // ---------- DELETE /api/membership/{id} ----------
    @Test
    void deleteMembership_ok() throws Exception {
        mockMvc.perform(delete("/api/membership/5"))
                .andExpect(status().isOk());
    }
}
