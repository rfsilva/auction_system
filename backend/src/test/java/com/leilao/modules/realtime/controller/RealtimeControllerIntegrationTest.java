package com.leilao.modules.realtime.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de integração para RealtimeController
 * CORRIGIDO: URLs ajustadas para considerar context-path /api
 */
@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
class RealtimeControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testBroadcastEndpoint() throws Exception {
        // URL corrigida: sem /api pois o MockMvc já considera o context-path
        mockMvc.perform(post("/realtime/broadcast")
                .param("message", "Teste de broadcast"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Evento enviado com sucesso"))
                .andExpect(jsonPath("$.sseClients").isNumber())
                .andExpect(jsonPath("$.eventData.message").value("Teste de broadcast"));
    }

    @Test
    void testStartSimulationEndpoint() throws Exception {
        mockMvc.perform(post("/realtime/start-simulation"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Simulação iniciada - eventos a cada 5 segundos"));
    }

    @Test
    void testStatsEndpoint() throws Exception {
        mockMvc.perform(get("/realtime/stats"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.sseConnections").isNumber())
                .andExpect(jsonPath("$.serverTime").isString())
                .andExpect(jsonPath("$.uptime").isNumber())
                .andExpect(jsonPath("$.webSocketEnabled").isBoolean());
    }

    @Test
    void testPingEndpoint() throws Exception {
        String pingData = objectMapper.writeValueAsString(
            java.util.Map.of("timestamp", System.currentTimeMillis())
        );

        mockMvc.perform(post("/realtime/ping")
                .contentType(MediaType.APPLICATION_JSON)
                .content(pingData))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("pong"))
                .andExpect(jsonPath("$.serverTimestamp").isNumber())
                .andExpect(jsonPath("$.latency").isNumber());
    }

    @Test
    void testPingEndpointWithoutBody() throws Exception {
        mockMvc.perform(post("/realtime/ping")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("pong"))
                .andExpect(jsonPath("$.serverTimestamp").isNumber());
    }

    @Test
    void testBroadcastWithSpecialCharacters() throws Exception {
        String specialMessage = "Teste com acentos: ção, ã, é, ü";
        
        mockMvc.perform(post("/realtime/broadcast")
                .param("message", specialMessage))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.eventData.message").value(specialMessage));
    }
}