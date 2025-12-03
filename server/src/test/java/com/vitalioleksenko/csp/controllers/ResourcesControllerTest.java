package com.vitalioleksenko.csp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitalioleksenko.csp.config.SecurityConfig;
import com.vitalioleksenko.csp.security.JwtFilter;
import com.vitalioleksenko.csp.services.ResourcesService; // припущення, піджени
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(ResourcesController.class)
@WebMvcTest(controllers = ResourcesController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                SecurityConfig.class,
                JwtFilter.class
        })
})
@AutoConfigureMockMvc(addFilters = false)
class ResourcesControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @TestConfiguration
    static class TestConfig {
        @Bean
        ResourcesService resourcesService() {
            return Mockito.mock(ResourcesService.class);
        }
    }

    @Autowired
    ResourcesService resourcesService;

    // ---------- GET /api/resource ----------
    @Test
    void readAllResources_ok() throws Exception {
        Page<?> page = new PageImpl<>(Collections.emptyList());
        Mockito.when(resourcesService.findAll(any(), anyInt(), anyInt()))
                .thenReturn((Page) page);

        mockMvc.perform(get("/api/resource"))
                .andExpect(status().isOk());
    }

    // ---------- POST /api/resource ----------
    @Test
    void createResource_ok() throws Exception {
        String body = """
            {
              "taskId": 1,
              "userId": 2,
              "title": "Some resource",
              "type": "MATERIAL",
              "format": "URL",
              "pathOrUrl": "https://example.com"
            }
            """;

        mockMvc.perform(post("/api/resource")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());
    }

    // ---------- GET /api/resource/{id} ----------
    @Test
    void readResource_ok() throws Exception {
        Mockito.when(resourcesService.findById(anyInt())).thenReturn(null);

        mockMvc.perform(get("/api/resource/5"))
                .andExpect(status().isOk());
    }

    // ---------- PATCH /api/resource/{id} ----------
    @Test
    void updateResource_ok() throws Exception {
        String body = """
            {
              "title": "Updated resource",
              "type": "SUBMISSION",
              "format": "FILE",
              "pathOrUrl": "/some/path"
            }
            """;

        mockMvc.perform(patch("/api/resource/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());
    }

    // ---------- DELETE /api/resource/{id} ----------
    @Test
    void deleteResource_ok() throws Exception {
        mockMvc.perform(delete("/api/resource/5"))
                .andExpect(status().isOk());
    }

    // ---------- FILE: POST /api/resource/{id}/file ----------
    @Test
    void uploadFile_ok() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.txt", "text/plain", "hello".getBytes()
        );

        mockMvc.perform(multipart("/api/resource/5/file").file(file))
                .andExpect(status().isOk());
    }

    // ---------- FILE: GET /api/resource/{id}/file ----------
    @Test
    void downloadFile_ok() throws Exception {
        mockMvc.perform(get("/api/resource/5/file"))
                .andExpect(status().isOk());
    }

    // ---------- FILE: DELETE /api/resource/{id}/file ----------
    @Test
    void deleteFile_ok() throws Exception {
        mockMvc.perform(delete("/api/resource/5/file"))
                .andExpect(status().isOk());
    }
}
