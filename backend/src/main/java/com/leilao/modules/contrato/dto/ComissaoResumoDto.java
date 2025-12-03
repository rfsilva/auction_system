package com.leilao.modules.contrato.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para resumo de comissões
 * História 2: Sistema de Cálculo de Comissões - Sprint S2.2
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComissaoResumoDto {
    
    /**
     * Total de comissões no período
     */
    private BigDecimal totalComissoes;
    
    /**
     * Total de vendas no período
     */
    private BigDecimal totalVendas;
    
    /**
     * Número total de transações
     */
    private Long numeroTransacoes;
    
    /**
     * Taxa média de comissão
     */
    private BigDecimal taxaMediaComissao;
    
    /**
     * Comissões realizadas (pagas)
     */
    private BigDecimal comissoesRealizadas;
    
    /**
     * Comissões projetadas (pendentes)
     */
    private BigDecimal comissoesProjetadas;
    
    /**
     * Número de contratos com vendas
     */
    private Long contratosComVendas;
}