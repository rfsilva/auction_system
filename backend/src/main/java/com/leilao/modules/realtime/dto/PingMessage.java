package com.leilao.modules.realtime.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * DTO para mensagens de ping/latência via WebSocket
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PingMessage extends WebSocketMessage {
    
    private String testId;
    private Integer sequenceNumber;
    
    // Constructors
    public PingMessage() {
        super();
    }
    
    public PingMessage(String testId, Integer sequenceNumber) {
        this.testId = testId;
        this.sequenceNumber = sequenceNumber;
        setType("ping");
    }
    
    // Getters and Setters
    public String getTestId() {
        return testId;
    }
    
    public void setTestId(String testId) {
        this.testId = testId;
    }
    
    public Integer getSequenceNumber() {
        return sequenceNumber;
    }
    
    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
    
    @Override
    public String toString() {
        return "PingMessage{" +
                "testId='" + testId + '\'' +
                ", sequenceNumber=" + sequenceNumber +
                ", type='" + getType() + '\'' +
                ", clientId='" + getClientId() + '\'' +
                ", timestamp=" + getTimestamp() +
                ", message='" + getMessage() + '\'' +
                '}';
    }
}