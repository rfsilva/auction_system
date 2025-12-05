package com.leilao.modules.realtime.controller;

import com.leilao.shared.util.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Controller para comunicação em tempo real (SSE e WebSocket)
 */
@RestController
@RequestMapping("/realtime")
@CrossOrigin(origins = "*")
@Slf4j
public class RealtimeController {
    
    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
    
    @Autowired(required = false)
    private SimpMessagingTemplate messagingTemplate;

    /**
     * Endpoint SSE para espectadores (read-only)
     */
    @GetMapping(value = "/sse/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamEvents() {
        SseEmitter emitter = new SseEmitter(0L); // Sem timeout
        
        emitter.onCompletion(() -> {
            emitters.remove(emitter);
            log.debug("SSE emitter removido - completion");
        });
        
        emitter.onTimeout(() -> {
            emitters.remove(emitter);
            log.debug("SSE emitter removido - timeout");
        });
        
        emitter.onError((ex) -> {
            emitters.remove(emitter);
            log.error("SSE emitter erro: {}", ex.getMessage());
        });
        
        emitters.add(emitter);
        log.info("Novo cliente SSE conectado. Total: {}", emitters.size());
        
        // Enviar evento de conexão
        try {
            Map<String, Object> connectionData = new HashMap<>();
            connectionData.put("message", MessageUtils.getMessage("websocket.connected"));
            connectionData.put("timestamp", System.currentTimeMillis());
            connectionData.put("serverTime", LocalDateTime.now().toString());
            connectionData.put("clientId", emitter.hashCode());
            
            emitter.send(SseEmitter.event()
                    .name("connected")
                    .data(connectionData));
        } catch (IOException e) {
            log.error("Erro ao enviar evento de conexão: {}", e.getMessage());
            emitter.completeWithError(e);
        }
        
        return emitter;
    }

    /**
     * Endpoint para testar broadcast de eventos
     */
    @PostMapping("/broadcast")
    public ResponseEntity<Map<String, Object>> broadcastEvent(@RequestParam String message) {
        log.info("Broadcast solicitado: {}", message);
        
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("message", message);
        eventData.put("timestamp", System.currentTimeMillis());
        eventData.put("serverTime", LocalDateTime.now().toString());
        eventData.put("source", "manual-broadcast");
        
        // Broadcast via SSE
        int sseClients = broadcastToSSE("test-event", eventData);
        
        // Broadcast via WebSocket (se disponível)
        broadcastToWebSocket("/topic/broadcast", eventData);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", MessageUtils.getMessage("websocket.event.sent"));
        response.put("sseClients", sseClients);
        response.put("eventData", eventData);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Simulação de eventos periódicos para teste
     */
    @PostMapping("/start-simulation")
    public ResponseEntity<Map<String, Object>> startSimulation() {
        log.info("Iniciando simulação de eventos");
        
        executor.scheduleAtFixedRate(() -> {
            Map<String, Object> simulationData = new HashMap<>();
            simulationData.put("message", MessageUtils.getMessage("websocket.simulated.event"));
            simulationData.put("timestamp", System.currentTimeMillis());
            simulationData.put("serverTime", LocalDateTime.now().toString());
            simulationData.put("sequence", System.currentTimeMillis() % 1000);
            simulationData.put("source", "simulation");
            
            // Broadcast via SSE
            broadcastToSSE("simulation", simulationData);
            
            // Broadcast via WebSocket (se disponível)
            broadcastToWebSocket("/topic/simulation", simulationData);
            
        }, 0, 5, TimeUnit.SECONDS);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", MessageUtils.getMessage("websocket.simulation.started"));
        response.put("interval", "5 segundos");
        
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para obter estatísticas das conexões
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getConnectionStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("sseConnections", emitters.size());
        stats.put("serverTime", LocalDateTime.now().toString());
        stats.put("uptime", System.currentTimeMillis());
        stats.put("webSocketEnabled", messagingTemplate != null);
        
        return ResponseEntity.ok(stats);
    }

    /**
     * Endpoint para teste de latência via HTTP
     */
    @PostMapping("/ping")
    public ResponseEntity<Map<String, Object>> ping(@RequestBody(required = false) Map<String, Object> pingData) {
        long serverTime = System.currentTimeMillis();
        
        Map<String, Object> response = new HashMap<>();
        response.put("type", "pong");
        response.put("serverTimestamp", serverTime);
        response.put("serverTime", LocalDateTime.now().toString());
        
        if (pingData != null && pingData.containsKey("timestamp")) {
            try {
                long clientTime = ((Number) pingData.get("timestamp")).longValue();
                response.put("clientTimestamp", clientTime);
                response.put("latency", serverTime - clientTime);
            } catch (Exception e) {
                log.warn("Erro ao calcular latência: {}", e.getMessage());
            }
        }
        
        return ResponseEntity.ok(response);
    }

    /**
     * Método para broadcast de eventos para todos os clientes SSE conectados
     */
    public int broadcastToSSE(String eventName, Object data) {
        int successCount = 0;
        
        emitters.removeIf(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .name(eventName)
                        .data(data));
                return false; // Manter emitter
            } catch (IOException e) {
                log.warn("Erro ao enviar evento SSE, removendo emitter: {}", e.getMessage());
                return true; // Remove emitter com erro
            }
        });
        
        successCount = emitters.size();
        log.debug("Evento '{}' enviado para {} clientes SSE", eventName, successCount);
        
        return successCount;
    }

    /**
     * Método para broadcast via WebSocket (se disponível)
     */
    private void broadcastToWebSocket(String destination, Object data) {
        if (messagingTemplate != null) {
            try {
                messagingTemplate.convertAndSend(destination, data);
                log.debug("Evento enviado via WebSocket para: {}", destination);
            } catch (Exception e) {
                log.warn("Erro ao enviar evento via WebSocket: {}", e.getMessage());
            }
        } else {
            log.debug("WebSocket não disponível, pulando broadcast para: {}", destination);
        }
    }
}