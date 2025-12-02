package com.leilao.modules.lote.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para criação de Lote
 * Atualizado para incluir contractId (ID do contrato escolhido pelo vendedor)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoteCreateRequest {

    @NotBlank(message = "Título é obrigatório")
    @Size(max = 255, message = "Título deve ter no máximo 255 caracteres")
    private String title;

    @Size(max = 5000, message = "Descrição deve ter no máximo 5000 caracteres")
    private String description;

    @NotNull(message = "Data de encerramento é obrigatória")
    @Future(message = "Data de encerramento deve ser no futuro")
    private LocalDateTime loteEndDateTime;

    // ID do contrato escolhido pelo vendedor (prioritário)
    @Size(max = 36, message = "ID do contrato inválido")
    private String contractId;

    // Categoria para buscar o contrato apropriado (alternativa ao contractId)
    @Size(max = 100, message = "Categoria deve ter no máximo 100 caracteres")
    private String categoria;

    // Lista de IDs dos produtos a serem associados ao lote
    private List<String> produtoIds;
}