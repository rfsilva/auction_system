package com.leilao.modules.admin.dto;

import com.leilao.shared.enums.UserRole;
import com.leilao.shared.enums.UserStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO para gestão administrativa de usuários
 */
@Data
public class AdminUsuarioDto {
    private String id;
    private String nome;
    private String email;
    private String telefone;
    private UserStatus status;
    private Set<UserRole> roles;
    private Boolean emailVerificado;
    private Boolean telefoneVerificado;
    private LocalDateTime ultimoLogin;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Campos calculados
    private Boolean isActive;
    private Boolean isBlocked;
    private Boolean isVendedor;
    private Boolean temContratoAtivo;
    private Integer totalContratos;
}