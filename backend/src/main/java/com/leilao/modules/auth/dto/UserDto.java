package com.leilao.modules.auth.dto;

import com.leilao.shared.enums.UserRole;
import com.leilao.shared.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO para dados do usuário
 * PADRONIZAÇÃO: Usando String para ID (compatível com VARCHAR(36))
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String id; // ✅ CORRIGIDO: String em vez de UUID
    private String name;
    private String email;
    private String phone;
    private UserStatus status;
    private Set<UserRole> roles;
    private Boolean emailVerificado;
    private Boolean telefoneVerificado;
    private LocalDateTime ultimoLogin;

    // Construtor adicional para casos básicos
    public UserDto(String id, String name, String email, UserStatus status, Set<UserRole> roles) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.status = status;
        this.roles = roles;
    }
}