package com.leilao.modules.contrato.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO para relatório completo de comissões
 * História 2: Sistema de Cálculo de Comissões - Sprint S2.2
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComissaoRelatorioDto {
    
    /**
     * Período do relatório
     */
    private PeriodoDto periodo;
    
    /**
     * Resumo das comissões
     */
    private ComissaoResumoDto resumo;
    
    /**
     * Detalhamento por contrato
     */
    private List<ComissaoDto> porContrato;
}