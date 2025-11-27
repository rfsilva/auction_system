package com.leilao.modules.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * DTO para requisição de refresh token
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "refreshToken") // Excluir token do toString por segurança
public class RefreshTokenRequest {

    @NotBlank(message = "Refresh token é obrigatório")
    private String refreshToken;
}