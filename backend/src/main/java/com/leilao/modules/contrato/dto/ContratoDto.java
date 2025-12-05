package com.leilao.modules.contrato.dto;

import com.leilao.shared.enums.ContractStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para resposta de contrato
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContratoDto {
    
    private String id; // String para compatibilidade
    private String sellerId; // String para compatibilidade
    private String sellerName; // Nome do vendedor (join)
    private String sellerCompanyName; // Nome da empresa do vendedor
    private BigDecimal feeRate;
    private String terms;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;
    private Boolean active;
    private String categoria;
    private ContractStatus status;
    private String createdBy;
    private String createdByName; // Nome do admin que criou
    private String approvedBy;
    private String approvedByName; // Nome do admin que aprovou
    private LocalDateTime approvedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Campos calculados
    private Boolean isActive;
    private Boolean isExpired;
    private Boolean canBeActivated;
    private Boolean canBeCancelled;
    private Boolean canBeEdited;
    private Long daysUntilExpiration;
    private String statusDisplayName;
}