package com.leilao.modules.lote.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para atualização de Lote
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoteUpdateRequest {

    @Size(max = 255, message = "Título deve ter no máximo 255 caracteres")
    private String title;

    @Size(max = 5000, message = "Descrição deve ter no máximo 5000 caracteres")
    private String description;

    @Future(message = "Data de encerramento deve ser no futuro")
    private LocalDateTime loteEndDateTime;

    // Lista de IDs dos produtos a serem associados ao lote
    private List<String> produtoIds;
}