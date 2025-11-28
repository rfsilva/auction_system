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
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoteDto {
    
    private String id;
    private String sellerId;
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
}