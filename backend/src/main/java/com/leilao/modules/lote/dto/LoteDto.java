package com.leilao.modules.lote.dto;

import com.leilao.shared.enums.LoteStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para representação de Lote
 * Atualizado para usar contractId ao invés de sellerId
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoteDto {
    
    private String id;
    private String contractId;
    private String title;
    private String description;
    private LocalDateTime loteEndDateTime;
    private LoteStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Campos calculados
    private boolean isActive;
    private boolean isExpired;
    private boolean canBeEdited;
    private boolean canBeActivated;
    private boolean canBeCancelled;
    private long timeRemaining; // em segundos
    
    // Lista de produtos associados (opcional)
    private List<String> produtoIds;
    private int totalProdutos;
    
    // Informações do contrato e vendedor (para exibição)
    private String sellerId; // Obtido através do contrato
    private String sellerName; // Nome do vendedor
    private String sellerCompanyName; // Nome da empresa do vendedor
    private String contractStatus; // Status do contrato
    private String categoria; // Categoria do contrato
    private String imagemDestaque;
}