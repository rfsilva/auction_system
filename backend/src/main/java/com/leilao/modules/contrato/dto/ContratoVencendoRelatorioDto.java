package com.leilao.modules.contrato.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO para relatório completo de contratos vencendo
 * História 3: Relatórios de Contratos Vencendo - Sprint S2.2
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContratoVencendoRelatorioDto {
    
    /**
     * Lista de contratos vencendo
     */
    private List<ContratoVencendoDto> contratos;
    
    /**
     * Resumo estatístico
     */
    private ContratoVencendoResumoDto resumo;
    
    /**
     * Parâmetros utilizados na consulta
     */
    private ContratoVencendoFiltroDto filtros;
}