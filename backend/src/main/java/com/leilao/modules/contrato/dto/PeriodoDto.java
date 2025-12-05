package com.leilao.modules.contrato.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO para representar um período de tempo
 * História 2: Sistema de Cálculo de Comissões - Sprint S2.2
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PeriodoDto {
    
    /**
     * Data de início do período
     */
    private LocalDate inicio;
    
    /**
     * Data de fim do período
     */
    private LocalDate fim;
    
    /**
     * Descrição do período (ex: "Janeiro 2024", "Semana 1", etc.)
     */
    private String descricao;
}