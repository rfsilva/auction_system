package com.leilao.modules.realtime.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * DTO base para mensagens WebSocket
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WebSocketMessage {
    
    private String type;
    private String clientId;
    private Long timestamp;
    private String message;
    
    // Constructors
    public WebSocketMessage() {}
    
    public WebSocketMessage(String type, String clientId, Long timestamp, String message) {
        this.type = type;
        this.clientId = clientId;
        this.timestamp = timestamp;
        this.message = message;
    }
    
    // Getters and Setters
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getClientId() {
        return clientId;
    }
    
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
    
    public Long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    @Override
    public String toString() {
        return "WebSocketMessage{" +
                "type='" + type + '\'' +
                ", clientId='" + clientId + '\'' +
                ", timestamp=" + timestamp +
                ", message='" + message + '\'' +
                '}';
    }
}