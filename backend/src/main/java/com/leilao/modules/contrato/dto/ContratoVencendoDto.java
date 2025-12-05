package com.leilao.modules.contrato.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para contratos próximos ao vencimento
 * História 3: Relatórios de Contratos Vencendo - Sprint S2.2
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContratoVencendoDto {
    
    /**
     * ID do contrato
     */
    private String id;
    
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
     * Data de validade do contrato
     */
    private LocalDateTime validTo;
    
    /**
     * Dias restantes até o vencimento
     */
    private Long diasRestantes;
    
    /**
     * Status do contrato
     */
    private String status;
    
    /**
     * Nível de urgência baseado nos dias restantes
     */
    private UrgenciaEnum urgencia;
    
    /**
     * Se o vendedor já foi notificado sobre o vencimento
     */
    private Boolean notificado;
    
    /**
     * Taxa de comissão do contrato
     */
    private java.math.BigDecimal taxaComissao;
    
    /**
     * Data de criação do contrato
     */
    private LocalDateTime createdAt;
    
    /**
     * Enum para níveis de urgência
     */
    public enum UrgenciaEnum {
        ALTA,    // <= 7 dias
        MEDIA,   // 8-15 dias
        BAIXA    // 16-30 dias
    }
}