package com.leilao.modules.realtime.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para mensagens de lance via WebSocket
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BidMessage extends WebSocketMessage {
    
    private String productId;
    private BigDecimal amount;
    private String userId;
    private String currency = "BRL";
    
    public BidMessage(String productId, BigDecimal amount, String userId) {
        this.productId = productId;
        this.amount = amount;
        this.userId = userId;
        this.currency = "BRL";
        setType("bid");
    }
}