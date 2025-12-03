package com.leilao.modules.contrato.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para resumo de contratos vencendo
 * História 3: Relatórios de Contratos Vencendo - Sprint S2.2
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContratoVencendoResumoDto {
    
    /**
     * Total de contratos vencendo
     */
    private Long total;
    
    /**
     * Contratos com urgência alta (≤ 7 dias)
     */
    private Long urgenciaAlta;
    
    /**
     * Contratos com urgência média (8-15 dias)
     */
    private Long urgenciaMedia;
    
    /**
     * Contratos com urgência baixa (16-30 dias)
     */
    private Long urgenciaBaixa;
    
    /**
     * Contratos já notificados
     */
    private Long notificados;
    
    /**
     * Contratos pendentes de notificação
     */
    private Long pendentesNotificacao;
}