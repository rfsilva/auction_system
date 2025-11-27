package com.leilao.modules.produto.dto;

import com.leilao.shared.enums.ProdutoStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para transferência de dados de Produto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoDto {
    
    private String id;
    private String sellerId;
    private String loteId;
    private String title;
    private String description;
    private List<String> images;
    private BigDecimal weight;
    private String dimensions;
    private BigDecimal initialPrice;
    private BigDecimal currentPrice;
    private BigDecimal reservePrice;
    private BigDecimal incrementMin;
    private LocalDateTime endDateTime;
    private ProdutoStatus status;
    private Boolean moderated;
    private String moderatorId;
    private LocalDateTime moderatedAt;
    private Boolean antiSnipeEnabled;
    private Integer antiSnipeExtension;
    private String categoria;
    private List<String> tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Campos calculados
    private Boolean isActive;
    private Boolean isExpired;
    private Boolean canReceiveBids;
    private Long timeRemaining; // em segundos
}