package com.englishlearningcopilot.backend.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.mock.web.MockHttpServletRequest;

class AdminPlaceholderControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new AdminPlaceholderController()).build();
    }

    @Test
    void placeholderReturnsResourceAndOperationForCollectionPath() throws Exception {
        mockMvc.perform(get("/api/admin/question-types"))
                .andExpect(status().isNotImplemented())
                .andExpect(jsonPath("$.resource").value("question-types"))
                .andExpect(jsonPath("$.operation").value("GET"));
    }

    @Test
    void placeholderReturnsTopLevelResourceForItemPath() throws Exception {
        mockMvc.perform(delete("/api/admin/vocabulary-entries/12"))
                .andExpect(status().isNotImplemented())
                .andExpect(jsonPath("$.resource").value("vocabulary-entries"))
                .andExpect(jsonPath("$.operation").value("DELETE"));
    }

    @Test
    void placeholderFallsBackToUnknownForUnexpectedPrefix() {
        AdminPlaceholderController controller = new AdminPlaceholderController();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/unexpected/path");

        org.assertj.core.api.Assertions.assertThat(controller.placeholder(request).getBody().resource())
                .isEqualTo("unknown");
    }
}
