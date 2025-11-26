package com.leilao.modules.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * DTO para resposta de autenticação
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"token", "refreshToken"}) // Excluir tokens sensíveis do toString
public class AuthResponse {

    private String token;
    private String refreshToken;
    private UserDto user;
}