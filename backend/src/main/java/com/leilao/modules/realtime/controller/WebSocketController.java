package com.leilao.modules.realtime.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * Controller WebSocket para comunicação bidirecional (bidders)
 */
@Controller
public class WebSocketController {

    /**
     * Endpoint WebSocket para receber mensagens de teste
     */
    @MessageMapping("/test")
    @SendTo("/topic/test")
    public String handleTestMessage(String message) {
        return "Echo: " + message + " - " + System.currentTimeMillis();
    }

    /**
     * Endpoint WebSocket para simular lances
     */
    @MessageMapping("/bid")
    @SendTo("/topic/bids")
    public String handleBid(String bidData) {
        // TODO: Implementar lógica de lance
        return "Lance recebido: " + bidData;
    }
}