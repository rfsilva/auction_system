package com.leilao.modules.auth.dto;

import com.leilao.shared.enums.UserRole;
import com.leilao.shared.enums.UserStatus;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO para dados do usuário
 */
public class UserDto {

    private String id;
    private String name;
    private String email;
    private String phone;
    private UserStatus status;
    private Set<UserRole> roles;
    private Boolean emailVerificado;
    private Boolean telefoneVerificado;
    private LocalDateTime ultimoLogin;

    // Constructors
    public UserDto() {}

    public UserDto(String id, String name, String email, UserStatus status, Set<UserRole> roles) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.status = status;
        this.roles = roles;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }

    public Boolean getEmailVerificado() {
        return emailVerificado;
    }

    public void setEmailVerificado(Boolean emailVerificado) {
        this.emailVerificado = emailVerificado;
    }

    public Boolean getTelefoneVerificado() {
        return telefoneVerificado;
    }

    public void setTelefoneVerificado(Boolean telefoneVerificado) {
        this.telefoneVerificado = telefoneVerificado;
    }

    public LocalDateTime getUltimoLogin() {
        return ultimoLogin;
    }

    public void setUltimoLogin(LocalDateTime ultimoLogin) {
        this.ultimoLogin = ultimoLogin;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", status=" + status +
                ", roles=" + roles +
                '}';
    }
}