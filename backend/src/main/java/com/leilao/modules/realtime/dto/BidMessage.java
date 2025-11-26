package com.leilao.modules.realtime.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

/**
 * DTO para mensagens de lance via WebSocket
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BidMessage extends WebSocketMessage {
    
    private String productId;
    private BigDecimal amount;
    private String userId;
    private String currency;
    
    // Constructors
    public BidMessage() {
        super();
    }
    
    public BidMessage(String productId, BigDecimal amount, String userId) {
        this.productId = productId;
        this.amount = amount;
        this.userId = userId;
        this.currency = "BRL";
        setType("bid");
    }
    
    // Getters and Setters
    public String getProductId() {
        return productId;
    }
    
    public void setProductId(String productId) {
        this.productId = productId;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
    @Override
    public String toString() {
        return "BidMessage{" +
                "productId='" + productId + '\'' +
                ", amount=" + amount +
                ", userId='" + userId + '\'' +
                ", currency='" + currency + '\'' +
                ", type='" + getType() + '\'' +
                ", clientId='" + getClientId() + '\'' +
                ", timestamp=" + getTimestamp() +
                '}';
    }
}