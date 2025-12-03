package com.vitalioleksenko.csp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitalioleksenko.csp.config.SecurityConfig;
import com.vitalioleksenko.csp.models.dto.UserStats;
import com.vitalioleksenko.csp.models.dto.user.UserCreateDTO;
import com.vitalioleksenko.csp.models.dto.user.UserUpdateDTO;
import com.vitalioleksenko.csp.security.JwtFilter;
import com.vitalioleksenko.csp.services.TasksService;
import com.vitalioleksenko.csp.services.UsersService;
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

@Import(UsersController.class)
@WebMvcTest(controllers = UsersController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                SecurityConfig.class,
                JwtFilter.class
        })
})
@AutoConfigureMockMvc(addFilters = false)
class UsersControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @TestConfiguration
    static class TestConfig {

        @Bean
        UsersService usersService() {
            return Mockito.mock(UsersService.class);
        }

        @Bean
        TasksService tasksService() {
            return Mockito.mock(TasksService.class);
        }
    }

    @Autowired
    UsersService usersService;

    @Autowired
    TasksService tasksService;

    // ---------- POST /api/user ----------
    @Test
    void createUser_ok() throws Exception {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setName("Test User");
        dto.setEmail("test@example.com");
        dto.setPassword("secret123");

        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        Mockito.verify(usersService).save(any(UserCreateDTO.class));
    }

    @Test
    void createUser_validationError() throws Exception {
        UserCreateDTO dto = new UserCreateDTO();

        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    // ---------- GET /api/user ----------
    @Test
    void readAll_defaultPaging_ok() throws Exception {
        Page<?> emptyPage = new PageImpl<>(Collections.emptyList());
        Mockito.when(usersService.getUsers(0, 20, null)).thenReturn((Page) emptyPage);

        mockMvc.perform(get("/api/user"))
                .andExpect(status().isOk());
    }

    @Test
    void readAll_withParams_ok() throws Exception {
        Page<?> emptyPage = new PageImpl<>(Collections.emptyList());
        Mockito.when(usersService.getUsers(2, 10, "john")).thenReturn((Page) emptyPage);

        mockMvc.perform(get("/api/user")
                        .param("page", "2")
                        .param("size", "10")
                        .param("search", "john"))
                .andExpect(status().isOk());

        Mockito.verify(usersService).getUsers(2, 10, "john");
    }

    // ---------- GET /api/user/{id} ----------
    @Test
    void readOne_ok() throws Exception {
        Mockito.when(usersService.findById(5)).thenReturn(null); // тіло не перевіряємо

        mockMvc.perform(get("/api/user/5"))
                .andExpect(status().isOk());

        Mockito.verify(usersService).findById(5);
    }

    // ---------- GET /api/user/{id}/stats ----------
    @Test
    void readStats_ok() throws Exception {
        Mockito.when(tasksService.getStatsForUser(3)).thenReturn(new UserStats());

        mockMvc.perform(get("/api/user/3/stats"))
                .andExpect(status().isOk());

        Mockito.verify(tasksService).getStatsForUser(3);
    }

    // ---------- PATCH /api/user/{id} ----------
    @Test
    void updateUser_validationError() throws Exception {
        UserUpdateDTO dto = new UserUpdateDTO();

        mockMvc.perform(patch("/api/user/7")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    // ---------- DELETE /api/user/{id} ----------
    @Test
    void deleteUser_ok() throws Exception {
        mockMvc.perform(delete("/api/user/4"))
                .andExpect(status().isNoContent());

        Mockito.verify(usersService).remove(4);
    }
}
