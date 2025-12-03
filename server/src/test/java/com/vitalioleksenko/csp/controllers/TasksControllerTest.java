package com.vitalioleksenko.csp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitalioleksenko.csp.config.SecurityConfig;
import com.vitalioleksenko.csp.security.JwtFilter;
import com.vitalioleksenko.csp.services.TasksService;
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

@Import(TasksController.class)
@WebMvcTest(controllers = TasksController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                SecurityConfig.class,
                JwtFilter.class
        })
})
@AutoConfigureMockMvc(addFilters = false)
class TasksControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @TestConfiguration
    static class TestConfig {
        @Bean
        TasksService tasksService() {
            return Mockito.mock(TasksService.class);
        }
    }

    @Autowired
    TasksService tasksService;

    // ---------- GET /api/task ----------
    @Test
    void readAllTasks_ok() throws Exception {
        Page<?> page = new PageImpl<>(Collections.emptyList());
        Mockito.when(tasksService.getTasks(any(), any(), any(), anyInt(), anyInt()))
                .thenReturn((Page) page); // метод вигаданий, піджени під свій

        mockMvc.perform(get("/api/task"))
                .andExpect(status().isOk());
    }

    // ---------- POST /api/task ----------
    @Test
    void createTask_ok() throws Exception {
        // мінімальний JSON по схемі TaskCreateDTO
        String body = """
            {
              "title": "Some task title",
              "groupId": 1,
              "userId": 2,
              "description": "Very long description at least 40 chars......",
              "status": "IN_PROGRESS"
            }
            """;

        mockMvc.perform(post("/api/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());
    }

    @Test
    void createTask_validationError() throws Exception {
        String body = """
            {
              "title": "Bad",
              "description": "short",
              "groupId": 1,
              "userId": 2
            }
            """;

        mockMvc.perform(post("/api/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    // ---------- GET /api/task/{id} ----------
    @Test
    void readTask_ok() throws Exception {
        Mockito.when(tasksService.getById(anyInt())).thenReturn(null); // піджени під свій метод

        mockMvc.perform(get("/api/task/10"))
                .andExpect(status().isOk());
    }

    // ---------- PATCH /api/task/{id} ----------
    @Test
    void updateTask_ok() throws Exception {
        String body = """
            {
              "title": "Updated title",
              "description": "Updated long description more than 40 chars............",
              "status": "DONE"
            }
            """;

        mockMvc.perform(patch("/api/task/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());
    }

    // ---------- DELETE /api/task/{id} ----------
    @Test
    void deleteTask_ok() throws Exception {
        mockMvc.perform(delete("/api/task/10"))
                .andExpect(status().isOk()); // у swagger стоїть 200
    }

    // ---------- GET /api/task/active ----------
    @Test
    void getActiveTasks_ok() throws Exception {
        Page<?> page = new PageImpl<>(Collections.emptyList());
        Mockito.when(tasksService.getActiveTasks(any(), any(), anyInt(), anyInt()))
                .thenReturn((Page) page); // знову ж – під свої методи

        mockMvc.perform(get("/api/task/active")
                        .param("groupId", "1")
                        .param("userId", "2")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());
    }
}
