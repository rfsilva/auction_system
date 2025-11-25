package com.leilao.modules.auth.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para requisição de refresh token
 */
public class RefreshTokenRequest {

    @NotBlank(message = "Refresh token é obrigatório")
    private String refreshToken;

    // Constructors
    public RefreshTokenRequest() {}

    public RefreshTokenRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    // Getters and Setters
    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public String toString() {
        return "RefreshTokenRequest{" +
                "refreshToken='[PROTECTED]'" +
                '}';
    }
}