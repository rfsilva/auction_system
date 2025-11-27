package com.leilao.shared.enums;

/**
 * Enum que define os papéis/roles dos usuários no sistema
 */
public enum UserRole {
    VISITOR("Visitante"),
    PARTICIPANT("Participante"),
    BUYER("Comprador"),
    SELLER("Vendedor"),
    ADMIN("Administrador");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}