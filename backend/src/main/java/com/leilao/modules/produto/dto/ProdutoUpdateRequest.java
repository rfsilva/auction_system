package com.leilao.modules.produto.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para atualização de produto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoUpdateRequest {

    @Size(max = 255, message = "Título deve ter no máximo 255 caracteres")
    private String title;

    @Size(max = 5000, message = "Descrição deve ter no máximo 5000 caracteres")
    private String description;

    private List<String> images;

    @DecimalMin(value = "0.001", message = "Peso deve ser maior que zero")
    private BigDecimal weight;

    private String dimensions;

    @DecimalMin(value = "0.01", message = "Preço inicial deve ser maior que zero")
    private BigDecimal initialPrice;

    @DecimalMin(value = "0.01", message = "Preço de reserva deve ser maior que zero")
    private BigDecimal reservePrice;

    @DecimalMin(value = "0.01", message = "Incremento mínimo deve ser maior que zero")
    private BigDecimal incrementMin;

    @Future(message = "Data de encerramento deve ser no futuro")
    private LocalDateTime endDateTime;

    @Size(max = 100, message = "Categoria deve ter no máximo 100 caracteres")
    private String categoria;

    private List<String> tags;

    private Boolean antiSnipeEnabled;

    @Min(value = 60, message = "Extensão anti-snipe deve ser de pelo menos 60 segundos")
    @Max(value = 3600, message = "Extensão anti-snipe deve ser de no máximo 3600 segundos")
    private Integer antiSnipeExtension;
}