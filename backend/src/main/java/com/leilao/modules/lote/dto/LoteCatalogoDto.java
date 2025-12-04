package com.leilao.modules.lote.dto;

import com.leilao.shared.enums.LoteStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO específico para o catálogo público de lotes
 * História 02: Transformação do Catálogo em Catálogo de Lotes
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoteCatalogoDto {
    
    private String id;
    private String title;
    private String description;
    private LocalDateTime loteEndDateTime;
    private LoteStatus status;
    private LocalDateTime createdAt;
    
    // Campos calculados
    private boolean isActive;
    private long timeRemaining; // em segundos
    
    // Informações dos produtos válidos
    private int quantidadeProdutosValidos;
    private String imagemDestaque; // Primeira imagem do primeiro produto válido
    
    // Informações do vendedor (para exibição)
    private String sellerName;
    private String sellerCompanyName;
    private String categoria;
}