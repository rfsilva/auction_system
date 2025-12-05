package com.leilao.modules.vendedor.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO para dados do vendedor
 */
@Data
public class VendedorDto {
    private String id;
    private String usuarioId;
    private String usuarioNome;
    private String companyName;
    private String taxId;
    private String contactEmail;
    private String contactPhone;
    private String description;
    private Boolean active;
    private Boolean verificado;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Campos calculados
    private Boolean temContratoAtivo;
    private Integer totalContratos;
}