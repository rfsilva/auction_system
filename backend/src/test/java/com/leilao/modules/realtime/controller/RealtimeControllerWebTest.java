package com.leilao.modules.realtime.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Teste web real para RealtimeController
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class RealtimeControllerWebTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testStatsEndpoint() {
        String url = "http://localhost:" + port + "/api/realtime/stats";
        
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("sseConnections"));
        assertTrue(response.getBody().containsKey("serverTime"));
        assertTrue(response.getBody().containsKey("uptime"));
    }

    @Test
    void testBroadcastEndpoint() {
        String url = "http://localhost:" + port + "/api/realtime/broadcast?message=teste";
        
        ResponseEntity<Map> response = restTemplate.postForEntity(url, null, Map.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals("Evento enviado com sucesso", response.getBody().get("message"));
    }

    @Test
    void testStartSimulationEndpoint() {
        String url = "http://localhost:" + port + "/api/realtime/start-simulation";
        
        ResponseEntity<Map> response = restTemplate.postForEntity(url, null, Map.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue((Boolean) response.getBody().get("success"));
        assertTrue(response.getBody().get("message").toString().contains("Simulação iniciada"));
    }

    @Test
    void testPingEndpoint() {
        String url = "http://localhost:" + port + "/api/realtime/ping";
        
        Map<String, Object> pingData = Map.of("timestamp", System.currentTimeMillis());
        
        ResponseEntity<Map> response = restTemplate.postForEntity(url, pingData, Map.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("pong", response.getBody().get("type"));
        assertTrue(response.getBody().containsKey("latency"));
    }
}