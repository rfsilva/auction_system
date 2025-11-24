package com.leilao.modules.realtime.controller;

import com.leilao.config.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes unitários para RealtimeController
 */
@WebMvcTest(RealtimeController.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
class RealtimeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testBroadcastEndpoint() throws Exception {
        mockMvc.perform(post("/realtime/broadcast")
                .param("message", "Test message"))
                .andExpect(status().isOk())
                .andExpect(content().string("Evento enviado para 0 clientes conectados"));
    }

    @Test
    void testStartSimulationEndpoint() throws Exception {
        mockMvc.perform(post("/realtime/start-simulation"))
                .andExpect(status().isOk())
                .andExpect(content().string("Simulação iniciada - eventos a cada 5 segundos"));
    }
}