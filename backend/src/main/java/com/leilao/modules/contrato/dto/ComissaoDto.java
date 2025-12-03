package com.leilao.modules.contrato.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO para informações de comissão por contrato
 * História 2: Sistema de Cálculo de Comissões - Sprint S2.2
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComissaoDto {
    
    /**
     * ID do contrato
     */
    private String contratoId;
    
    /**
     * Nome do vendedor
     */
    private String vendedorNome;
    
    /**
     * Nome da empresa do vendedor
     */
    private String vendedorEmpresa;
    
    /**
     * Categoria do contrato
     */
    private String categoria;
    
    /**
     * Taxa de comissão do contrato
     */
    private BigDecimal taxaComissao;
    
    /**
     * Total de vendas no período
     */
    private BigDecimal totalVendas;
    
    /**
     * Total de comissões no período
     */
    private BigDecimal totalComissoes;
    
    /**
     * Número de transações no período
     */
    private Long numeroTransacoes;
    
    /**
     * Data de início do período
     */
    private LocalDate periodoInicio;
    
    /**
     * Data de fim do período
     */
    private LocalDate periodoFim;
    
    /**
     * Comissões realizadas (pagas)
     */
    private BigDecimal comissoesRealizadas;
    
    /**
     * Comissões projetadas (pendentes)
     */
    private BigDecimal comissoesProjetadas;
}