package com.leilao.modules.contrato.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para criação de contrato
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContratoCreateRequest {

    @NotBlank(message = "ID do vendedor é obrigatório")
    private String sellerId; // String para compatibilidade

    @NotNull(message = "Taxa de comissão é obrigatória")
    @DecimalMin(value = "0.0001", message = "Taxa deve ser maior que 0.01%")
    @DecimalMax(value = "0.5000", message = "Taxa deve ser menor que 50%")
    @Digits(integer = 1, fraction = 4, message = "Taxa deve ter no máximo 4 casas decimais")
    private BigDecimal feeRate;

    @NotBlank(message = "Termos do contrato são obrigatórios")
    @Size(min = 50, max = 10000, message = "Termos devem ter entre 50 e 10000 caracteres")
    private String terms;

    @Future(message = "Data de início deve ser futura")
    private LocalDateTime validFrom;

    @Future(message = "Data de fim deve ser futura")
    private LocalDateTime validTo;

    @Size(max = 100, message = "Categoria deve ter no máximo 100 caracteres")
    private String categoria;

    // Validação customizada para datas
    @AssertTrue(message = "Data de fim deve ser posterior à data de início")
    public boolean isValidDateRange() {
        if (validFrom == null || validTo == null) {
            return true; // Deixa outras validações cuidarem dos nulls
        }
        return validTo.isAfter(validFrom);
    }

    // Validação customizada para período mínimo
    @AssertTrue(message = "Contrato deve ter duração mínima de 30 dias")
    public boolean isValidMinimumPeriod() {
        if (validFrom == null || validTo == null) {
            return true;
        }
        return validFrom.plusDays(30).isBefore(validTo) || validFrom.plusDays(30).isEqual(validTo);
    }
}