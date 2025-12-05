package com.leilao.modules.admin.dto;

import com.leilao.shared.enums.UserRole;
import com.leilao.shared.enums.UserStatus;
import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import java.util.Set;

/**
 * DTO para atualização de usuários pelo admin
 */
@Data
public class AdminUsuarioUpdateRequest {
    
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;
    
    @Email(message = "Email deve ter formato válido")
    private String email;
    
    @Size(max = 20, message = "Telefone deve ter no máximo 20 caracteres")
    private String telefone;
    
    private UserStatus status;
    
    private Set<UserRole> roles;
    
    private Boolean emailVerificado;
    
    private Boolean telefoneVerificado;
}