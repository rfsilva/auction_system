package com.leilao.modules.contrato.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Request para criar contrato a partir de usuário
 * Novo fluxo: usuário -> vendedor -> contrato
 */
@Data
public class ContratoCreateFromUserRequest {

    @NotBlank(message = "ID do usuário é obrigatório")
    private String usuarioId;

    @NotNull(message = "Taxa de comissão é obrigatória")
    @DecimalMin(value = "0.0001", message = "Taxa deve ser maior que 0.01%")
    @DecimalMax(value = "0.5000", message = "Taxa deve ser menor que 50%")
    private BigDecimal feeRate;

    @NotBlank(message = "Termos do contrato são obrigatórios")
    @Size(min = 50, max = 10000, message = "Termos devem ter entre 50 e 10.000 caracteres")
    private String terms;

    private LocalDateTime validFrom;
    private LocalDateTime validTo;
    private String categoria;
    
    // Flag para ativar imediatamente após criação
    private Boolean ativarImediatamente = false;
}