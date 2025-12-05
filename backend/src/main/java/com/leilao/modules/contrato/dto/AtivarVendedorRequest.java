package com.leilao.modules.contrato.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Request para ativar usuário como vendedor através de contrato
 * História 2: Processo de Contratação de Vendedores
 */
@Data
public class AtivarVendedorRequest {

    @NotBlank(message = "ID do usuário é obrigatório")
    private String usuarioId;

    @NotNull(message = "Taxa de comissão é obrigatória")
    @DecimalMin(value = "0.0001", message = "Taxa deve ser maior que 0.01%")
    @DecimalMax(value = "0.5", message = "Taxa deve ser menor que 50%")
    private BigDecimal feeRate;

    @NotBlank(message = "Termos do contrato são obrigatórios")
    @Size(min = 10, message = "Termos devem ter pelo menos 10 caracteres")
    private String terms;

    private LocalDateTime validFrom;

    private LocalDateTime validTo;

    private String categoria;

    private Boolean ativarImediatamente = true;
}