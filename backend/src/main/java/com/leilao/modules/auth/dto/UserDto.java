package com.leilao.modules.auth.dto;

import com.leilao.shared.enums.UserRole;
import com.leilao.shared.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * DTO para dados do usuário
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private UUID id;
    private String name;
    private String email;
    private String phone;
    private UserStatus status;
    private Set<UserRole> roles;
    private Boolean emailVerificado;
    private Boolean telefoneVerificado;
    private LocalDateTime ultimoLogin;

    // Construtor adicional para casos básicos
    public UserDto(UUID id, String name, String email, UserStatus status, Set<UserRole> roles) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.status = status;
        this.roles = roles;
    }
}