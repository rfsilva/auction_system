package com.leilao.modules.realtime.controller;

import com.leilao.modules.realtime.dto.BidMessage;
import com.leilao.modules.realtime.dto.PingMessage;
import com.leilao.modules.realtime.dto.WebSocketMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller WebSocket para comunicação bidirecional (bidders) com suporte a i18n usando MessageSourceAccessor
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class WebSocketController {
    
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageSourceAccessor messageSourceAccessor;

    /**
     * Endpoint WebSocket para receber mensagens de teste
     */
    @MessageMapping("/test")
    @SendTo("/topic/test")
    public Map<String, Object> handleTestMessage(WebSocketMessage message) {
        log.info("Mensagem de teste recebida: {}", message);
        
        Map<String, Object> response = new HashMap<>();
        response.put("type", "test-response");
        response.put("status", "success");
        response.put("originalMessage", message.getMessage());
        response.put("echo", "Echo: " + message.getMessage());
        response.put("serverTimestamp", System.currentTimeMillis());
        response.put("serverTime", LocalDateTime.now().toString());
        response.put("clientId", message.getClientId());
        response.put("clientTimestamp", message.getTimestamp());
        
        if (message.getTimestamp() != null) {
            response.put("processingTime", System.currentTimeMillis() - message.getTimestamp());
        }
        
        return response;
    }

    /**
     * Endpoint WebSocket para simular lances
     */
    @MessageMapping("/bid")
    @SendTo("/topic/bids")
    public Map<String, Object> handleBid(BidMessage bidMessage) {
        log.info("Lance recebido: {}", bidMessage);
        
        try {
            // Validações básicas
            if (bidMessage.getProductId() == null || bidMessage.getAmount() == null) {
                throw new IllegalArgumentException(
                        messageSourceAccessor.getMessage("websocket.bid.required.fields", LocaleContextHolder.getLocale()));
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("type", "bid-response");
            response.put("status", "accepted");
            response.put("bidId", "bid-" + System.currentTimeMillis());
            response.put("productId", bidMessage.getProductId());
            response.put("amount", bidMessage.getAmount());
            response.put("userId", bidMessage.getUserId());
            response.put("currency", bidMessage.getCurrency());
            response.put("serverTimestamp", System.currentTimeMillis());
            response.put("serverTime", LocalDateTime.now().toString());
            response.put("message", messageSourceAccessor.getMessage("websocket.bid.success", LocaleContextHolder.getLocale()));
            response.put("clientId", bidMessage.getClientId());
            response.put("clientTimestamp", bidMessage.getTimestamp());
            
            if (bidMessage.getTimestamp() != null) {
                response.put("processingTime", System.currentTimeMillis() - bidMessage.getTimestamp());
            }
            
            return response;
            
        } catch (Exception e) {
            log.error("Erro ao processar lance: {}", e.getMessage());
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("type", "bid-error");
            errorResponse.put("status", "rejected");
            errorResponse.put("error", e.getMessage());
            errorResponse.put("serverTimestamp", System.currentTimeMillis());
            errorResponse.put("clientId", bidMessage.getClientId());
            errorResponse.put("originalData", bidMessage);
            
            return errorResponse;
        }
    }

    /**
     * Endpoint para teste de latência
     */
    @MessageMapping("/ping")
    @SendTo("/topic/pong")
    public Map<String, Object> handlePing(PingMessage pingMessage) {
        log.debug("Ping recebido: {}", pingMessage);
        
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("type", "pong");
            response.put("status", "success");
            response.put("clientTimestamp", pingMessage.getTimestamp());
            response.put("serverTimestamp", System.currentTimeMillis());
            response.put("clientId", pingMessage.getClientId());
            response.put("testId", pingMessage.getTestId());
            response.put("sequenceNumber", pingMessage.getSequenceNumber());
            response.put("message", pingMessage.getMessage());
            
            if (pingMessage.getTimestamp() != null) {
                long latency = System.currentTimeMillis() - pingMessage.getTimestamp();
                response.put("latency", latency);
                response.put("latencyMs", latency + "ms");
            }
            
            return response;
            
        } catch (Exception e) {
            log.error("Erro ao processar ping: {}", e.getMessage());
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("type", "pong-error");
            errorResponse.put("status", "error");
            errorResponse.put("error", e.getMessage());
            errorResponse.put("serverTimestamp", System.currentTimeMillis());
            errorResponse.put("clientId", pingMessage.getClientId());
            
            return errorResponse;
        }
    }

    /**
     * Endpoint para simulação de concorrência
     */
    @MessageMapping("/simulation")
    @SendTo("/topic/simulation")
    public Map<String, Object> handleSimulation(WebSocketMessage simulationMessage) {
        log.debug("Dados de simulação recebidos: {}", simulationMessage);
        
        Map<String, Object> response = new HashMap<>();
        response.put("type", "simulation-response");
        response.put("status", "processed");
        response.put("serverTimestamp", System.currentTimeMillis());
        response.put("serverTime", LocalDateTime.now().toString());
        response.put("clientId", simulationMessage.getClientId());
        response.put("clientTimestamp", simulationMessage.getTimestamp());
        response.put("originalMessage", simulationMessage.getMessage());
        
        if (simulationMessage.getTimestamp() != null) {
            response.put("processingTime", System.currentTimeMillis() - simulationMessage.getTimestamp());
        }
        
        return response;
    }

    /**
     * Endpoint genérico para mensagens
     */
    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public Map<String, Object> handleMessage(WebSocketMessage message) {
        log.info("Mensagem genérica recebida: {}", message);
        
        Map<String, Object> response = new HashMap<>();
        response.put("type", "message-response");
        response.put("status", "received");
        response.put("serverTimestamp", System.currentTimeMillis());
        response.put("serverTime", LocalDateTime.now().toString());
        response.put("clientId", message.getClientId());
        response.put("clientTimestamp", message.getTimestamp());
        response.put("originalMessage", message.getMessage());
        response.put("echo", messageSourceAccessor.getMessage("websocket.message.received", LocaleContextHolder.getLocale()) + ": " + message.getMessage());
        
        if (message.getTimestamp() != null) {
            response.put("processingTime", System.currentTimeMillis() - message.getTimestamp());
        }
        
        return response;
    }

    /**
     * Endpoint para fallback - aceita Map genérico
     */
    @MessageMapping("/generic")
    @SendTo("/topic/generic")
    public Map<String, Object> handleGeneric(Map<String, Object> data) {
        log.info("Dados genéricos recebidos: {}", data);
        
        Map<String, Object> response = new HashMap<>();
        response.put("type", "generic-response");
        response.put("status", "processed");
        response.put("serverTimestamp", System.currentTimeMillis());
        response.put("serverTime", LocalDateTime.now().toString());
        response.put("originalData", data);
        response.put("dataType", data.getClass().getSimpleName());
        
        return response;
    }
}