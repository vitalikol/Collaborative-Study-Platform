package com.vitalioleksenko.csp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitalioleksenko.csp.config.SecurityConfig;
import com.vitalioleksenko.csp.security.JwtFilter;
import com.vitalioleksenko.csp.services.GroupsService; // припущення
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(GroupsController.class)
@WebMvcTest(controllers = GroupsController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                SecurityConfig.class,
                JwtFilter.class
        })
})
@AutoConfigureMockMvc(addFilters = false)
class GroupsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @TestConfiguration
    static class TestConfig {
        @Bean
        GroupsService groupsService() {
            return Mockito.mock(GroupsService.class);
        }
    }

    @Autowired
    GroupsService groupsService;

    // ---------- GET /api/group ----------
    @Test
    void readAllGroups_ok() throws Exception {
        Page<?> page = new PageImpl<>(Collections.emptyList());
        Mockito.when(groupsService.getGroups(any(), any(), anyInt(), anyInt()))
                .thenReturn((Page) page);

        mockMvc.perform(get("/api/group")
                        .param("search", "proj")
                        .param("userId", "1")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk());
    }

    // ---------- POST /api/group ----------
    @Test
    void createGroup_ok() throws Exception {
        String body = """
            {
              "name": "My group",
              "description": "Very long description about the group, >= 40 chars...",
              "createdBy": 1
            }
            """;

        mockMvc.perform(post("/api/group")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());
    }

    // ---------- GET /api/group/{id} ----------
    @Test
    void readGroup_ok() throws Exception {
        Mockito.when(groupsService.getById(anyInt())).thenReturn(null);

        mockMvc.perform(get("/api/group/3"))
                .andExpect(status().isOk());
    }

    // ---------- PATCH /api/group/{id} ----------
    @Test
    void updateGroup_ok() throws Exception {
        String body = """
            {
              "name": "Updated Group",
              "description": "Updated long group description .........................."
            }
            """;

        mockMvc.perform(patch("/api/group/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());
    }

    // ---------- DELETE /api/group/{id} ----------
    @Test
    void deleteGroup_ok() throws Exception {
        mockMvc.perform(delete("/api/group/3"))
                .andExpect(status().isOk());
    }
}
