package com.leilao.modules.realtime.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes simples para RealtimeController
 */
@SpringBootTest
@ActiveProfiles("test")
class RealtimeControllerSimpleTest {

    @Autowired
    private RealtimeController realtimeController;

    @Test
    void testControllerIsLoaded() {
        assertNotNull(realtimeController, "RealtimeController deve ser carregado pelo Spring");
    }

    @Test
    void testBroadcastMethod() {
        // Teste direto do método sem MockMvc
        var response = realtimeController.broadcastEvent("Teste direto");
        
        assertNotNull(response, "Response não deve ser null");
        assertEquals(200, response.getStatusCode().value(), "Status deve ser 200");
        
        var body = response.getBody();
        assertNotNull(body, "Body não deve ser null");
        assertTrue((Boolean) body.get("success"), "Success deve ser true");
        assertEquals("Evento enviado com sucesso", body.get("message"));
    }

    @Test
    void testStatsMethod() {
        // Teste direto do método sem MockMvc
        var response = realtimeController.getConnectionStats();
        
        assertNotNull(response, "Response não deve ser null");
        assertEquals(200, response.getStatusCode().value(), "Status deve ser 200");
        
        var body = response.getBody();
        assertNotNull(body, "Body não deve ser null");
        assertTrue(body.containsKey("sseConnections"), "Deve conter sseConnections");
        assertTrue(body.containsKey("serverTime"), "Deve conter serverTime");
        assertTrue(body.containsKey("uptime"), "Deve conter uptime");
    }

    @Test
    void testPingMethod() {
        // Teste direto do método sem MockMvc
        Map<String, Object> pingData = new HashMap<>();
        pingData.put("timestamp", System.currentTimeMillis());
        
        var response = realtimeController.ping(pingData);
        
        assertNotNull(response, "Response não deve ser null");
        assertEquals(200, response.getStatusCode().value(), "Status deve ser 200");
        
        var body = response.getBody();
        assertNotNull(body, "Body não deve ser null");
        assertEquals("pong", body.get("type"), "Type deve ser pong");
        assertTrue(body.containsKey("latency"), "Deve conter latency");
    }

    @Test
    void testPingMethodWithoutData() {
        // Teste direto do método sem MockMvc e sem dados
        var response = realtimeController.ping(null);
        
        assertNotNull(response, "Response não deve ser null");
        assertEquals(200, response.getStatusCode().value(), "Status deve ser 200");
        
        var body = response.getBody();
        assertNotNull(body, "Body não deve ser null");
        assertEquals("pong", body.get("type"), "Type deve ser pong");
        assertFalse(body.containsKey("latency"), "Não deve conter latency sem timestamp");
    }

    @Test
    void testStartSimulationMethod() {
        // Teste direto do método sem MockMvc
        var response = realtimeController.startSimulation();
        
        assertNotNull(response, "Response não deve ser null");
        assertEquals(200, response.getStatusCode().value(), "Status deve ser 200");
        
        var body = response.getBody();
        assertNotNull(body, "Body não deve ser null");
        assertTrue((Boolean) body.get("success"), "Success deve ser true");
        assertTrue(body.get("message").toString().contains("Simulação iniciada"));
    }
}