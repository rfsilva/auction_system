package com.leilao.modules.realtime.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller WebSocket para comunicação bidirecional (bidders)
 */
@Controller
public class WebSocketController {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketController.class);
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Endpoint WebSocket para receber mensagens de teste
     */
    @MessageMapping("/test")
    @SendTo("/topic/test")
    public Map<String, Object> handleTestMessage(String message) {
        logger.info("Mensagem de teste recebida: {}", message);
        
        Map<String, Object> response = new HashMap<>();
        response.put("type", "test-response");
        response.put("originalMessage", message);
        response.put("echo", "Echo: " + message);
        response.put("serverTimestamp", System.currentTimeMillis());
        response.put("serverTime", LocalDateTime.now().toString());
        
        return response;
    }

    /**
     * Endpoint WebSocket para simular lances
     */
    @MessageMapping("/bid")
    @SendTo("/topic/bids")
    public Map<String, Object> handleBid(String bidData) {
        logger.info("Lance recebido: {}", bidData);
        
        try {
            // Tentar parsear o JSON do lance
            Map<String, Object> bidInfo = objectMapper.readValue(bidData, Map.class);
            
            Map<String, Object> response = new HashMap<>();
            response.put("type", "bid-response");
            response.put("status", "accepted");
            response.put("bidId", "bid-" + System.currentTimeMillis());
            response.put("productId", bidInfo.get("productId"));
            response.put("amount", bidInfo.get("amount"));
            response.put("serverTimestamp", System.currentTimeMillis());
            response.put("serverTime", LocalDateTime.now().toString());
            response.put("message", "Lance processado com sucesso");
            
            return response;
            
        } catch (Exception e) {
            logger.error("Erro ao processar lance: {}", e.getMessage());
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("type", "bid-error");
            errorResponse.put("status", "rejected");
            errorResponse.put("error", "Formato de lance inválido");
            errorResponse.put("serverTimestamp", System.currentTimeMillis());
            
            return errorResponse;
        }
    }

    /**
     * Endpoint para teste de latência
     */
    @MessageMapping("/ping")
    @SendTo("/topic/pong")
    public Map<String, Object> handlePing(String pingData) {
        logger.debug("Ping recebido: {}", pingData);
        
        try {
            Map<String, Object> pingInfo = objectMapper.readValue(pingData, Map.class);
            Long clientTimestamp = ((Number) pingInfo.get("timestamp")).longValue();
            
            Map<String, Object> response = new HashMap<>();
            response.put("type", "pong");
            response.put("clientTimestamp", clientTimestamp);
            response.put("serverTimestamp", System.currentTimeMillis());
            response.put("latency", System.currentTimeMillis() - clientTimestamp);
            
            return response;
            
        } catch (Exception e) {
            logger.error("Erro ao processar ping: {}", e.getMessage());
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("type", "pong-error");
            errorResponse.put("error", "Formato de ping inválido");
            errorResponse.put("serverTimestamp", System.currentTimeMillis());
            
            return errorResponse;
        }
    }
}