package com.englishlearningcopilot.backend.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.englishlearningcopilot.backend.dto.UpdateUserRoleRequest;
import com.englishlearningcopilot.backend.dto.UpdateUserStatusRequest;
import com.englishlearningcopilot.backend.dto.UserResponse;
import com.englishlearningcopilot.backend.entity.UserRole;
import com.englishlearningcopilot.backend.exception.GlobalExceptionHandler;
import com.englishlearningcopilot.backend.service.AdminUserService;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@ExtendWith(MockitoExtension.class)
class AdminUserControllerTest {

    @Mock
    private AdminUserService adminUserService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(new AdminUserController(adminUserService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();
    }

    @Test
    void listUsersReturnsServiceUsers() throws Exception {
        when(adminUserService.listUsers()).thenReturn(List.of(user(UserRole.USER, true)));

        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("learner"));
    }

    @Test
    void updateRoleValidatesAndDelegates() throws Exception {
        when(adminUserService.updateRole(eq(7L), any(UpdateUserRoleRequest.class)))
                .thenReturn(user(UserRole.ADMIN, true));

        mockMvc.perform(patch("/api/admin/users/7/role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "role": "ADMIN"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("ADMIN"));

        verify(adminUserService).updateRole(eq(7L), any(UpdateUserRoleRequest.class));
    }

    @Test
    void updateRoleRejectsMissingRole() throws Exception {
        mockMvc.perform(patch("/api/admin/users/7/role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.role").exists());
    }

    @Test
    void updateStatusValidatesAndDelegates() throws Exception {
        when(adminUserService.updateStatus(eq(7L), any(UpdateUserStatusRequest.class)))
                .thenReturn(user(UserRole.USER, false));

        mockMvc.perform(patch("/api/admin/users/7/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "enabled": false
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enabled").value(false));

        verify(adminUserService).updateStatus(eq(7L), any(UpdateUserStatusRequest.class));
    }

    @Test
    void updateStatusRejectsMissingEnabledFlag() throws Exception {
        mockMvc.perform(patch("/api/admin/users/7/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.enabled").exists());
    }

    private static UserResponse user(UserRole role, boolean enabled) {
        Instant now = Instant.parse("2026-07-27T00:00:00Z");
        return new UserResponse(7L, "learner", "learner@example.com", "Learner", role, enabled, now, now, null);
    }
}
