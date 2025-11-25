package com.leilao.modules.auth.controller;

import com.leilao.modules.auth.service.AuthService;
import com.leilao.modules.auth.service.JwtService;
import com.leilao.modules.auth.service.UserDetailsServiceImpl;
import com.leilao.core.config.JwtAuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes unitários para AuthController com mocks adequados
 */
@WebMvcTest(AuthController.class)
@ActiveProfiles("test")
class AuthControllerTestFixed {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    @WithMockUser
    void testHealthEndpoint() throws Exception {
        mockMvc.perform(get("/auth/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("Auth service is running"));
    }

    @Test
    @WithMockUser
    void testCheckEmailEndpoint() throws Exception {
        mockMvc.perform(get("/auth/check-email")
                .param("email", "test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}