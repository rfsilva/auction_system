package com.leilao.modules.contrato.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para filtros de consulta de contratos vencendo
 * História 3: Relatórios de Contratos Vencendo - Sprint S2.2
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContratoVencendoFiltroDto {
    
    /**
     * Número de dias para considerar como "vencendo"
     */
    private Integer dias;
    
    /**
     * Se deve incluir contratos já notificados
     */
    private Boolean incluirNotificados;
    
    /**
     * Filtro por vendedor específico
     */
    private String vendedorId;
    
    /**
     * Filtro por categoria específica
     */
    private String categoria;
    
    /**
     * Filtro por nível de urgência
     */
    private ContratoVencendoDto.UrgenciaEnum urgencia;
}