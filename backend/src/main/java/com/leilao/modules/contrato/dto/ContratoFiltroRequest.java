package com.leilao.modules.contrato.dto;

import com.leilao.shared.enums.ContractStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para filtros de busca de contratos
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContratoFiltroRequest {

    private String sellerId; // String para compatibilidade
    private String sellerName;
    private ContractStatus status;
    private String categoria;
    private Boolean active;
    private BigDecimal feeRateMin;
    private BigDecimal feeRateMax;
    private LocalDateTime validFromStart;
    private LocalDateTime validFromEnd;
    private LocalDateTime validToStart;
    private LocalDateTime validToEnd;
    private Boolean expiringIn30Days;
    private Boolean expiringIn7Days;
    private String termo; // Busca geral nos termos do contrato
    
    // Paginação
    private Integer page = 0;
    private Integer size = 20;
    private String sort = "createdAt";
    private String direction = "desc";
}