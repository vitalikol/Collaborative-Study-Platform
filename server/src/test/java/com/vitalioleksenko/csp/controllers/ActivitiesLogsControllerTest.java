package com.vitalioleksenko.csp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitalioleksenko.csp.config.SecurityConfig;
import com.vitalioleksenko.csp.security.JwtFilter;
import com.vitalioleksenko.csp.services.ActivitiesLogsService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(ActivitiesLogsController.class)
@WebMvcTest(controllers = ActivitiesLogsController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                SecurityConfig.class,
                JwtFilter.class
        })
})
@AutoConfigureMockMvc(addFilters = false)
class ActivitiesLogsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @TestConfiguration
    static class TestConfig {
        @Bean
        ActivitiesLogsService activitiesLogsService() {
            return Mockito.mock(ActivitiesLogsService.class);
        }
    }

    @Autowired
    ActivitiesLogsService activitiesLogsService;

    // ---------- GET /api/log ----------
    @Test
    void readAllLogs_ok() throws Exception {
        Page<?> page = new PageImpl<>(Collections.emptyList());
        Mockito.when(activitiesLogsService.findAll(anyInt(), anyInt()))
                .thenReturn((Page) page);

        mockMvc.perform(get("/api/log")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk());
    }

    // ---------- GET /api/log/{id} ----------
    @Test
    void readLog_ok() throws Exception {
        Mockito.when(activitiesLogsService.findById(anyInt())).thenReturn(null);

        mockMvc.perform(get("/api/log/1"))
                .andExpect(status().isOk());
    }

    // ---------- GET /api/log/user/{id} ----------
    @Test
    void readLogsForUser_ok() throws Exception {
        Page<?> page = new PageImpl<>(Collections.emptyList());
        Mockito.when(activitiesLogsService.findAllForUser(anyInt(), anyInt(), anyInt()))
                .thenReturn((Page) page);

        mockMvc.perform(get("/api/log/user/1")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk());
    }
}
