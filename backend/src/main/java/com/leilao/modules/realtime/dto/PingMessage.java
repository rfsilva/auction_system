package com.leilao.modules.realtime.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * DTO para mensagens de ping/latência via WebSocket
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PingMessage extends WebSocketMessage {
    
    private String testId;
    private Integer sequenceNumber;
    
    public PingMessage(String testId, Integer sequenceNumber) {
        this.testId = testId;
        this.sequenceNumber = sequenceNumber;
        setType("ping");
    }
}