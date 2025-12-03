package com.leilao.modules.contrato.dto;

import com.leilao.shared.enums.ContractStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

/**
 * DTO para estatísticas consolidadas de contratos
 * História 1: Endpoints de Estatísticas de Contratos - Sprint S2.2
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContratoEstatisticasDto {
    
    /**
     * Total de contratos no sistema
     */
    private Long totalContratos;
    
    /**
     * Contratos agrupados por status
     */
    private Map<ContractStatus, Long> contratosPorStatus;
    
    /**
     * Número de vendedores com contratos ativos
     */
    private Long vendedoresAtivos;
    
    /**
     * Receita projetada para o mês atual baseada em contratos ativos
     */
    private BigDecimal receitaProjetadaMes;
    
    /**
     * Receita realizada no mês atual
     */
    private BigDecimal receitaRealizadaMes;
    
    /**
     * Taxa média de comissão dos contratos ativos
     */
    private BigDecimal taxaMediaComissao;
    
    /**
     * Número de contratos que vencem nos próximos 30 dias
     */
    private Long contratosVencendo30Dias;
    
    /**
     * Número de contratos criados no mês atual
     */
    private Long contratosCriadosMes;
    
    /**
     * Número de contratos expirados no mês atual
     */
    private Long contratosExpiradosMes;
    
    /**
     * Número de categorias distintas com contratos ativos
     */
    private Long categoriasAtivas;
}