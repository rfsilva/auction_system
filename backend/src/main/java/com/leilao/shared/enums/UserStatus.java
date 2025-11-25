package com.leilao.shared.enums;

/**
 * Enum que define os status possíveis de um usuário
 */
public enum UserStatus {
    PENDING_VERIFICATION("Aguardando Verificação"),
    ACTIVE("Ativo"),
    INACTIVE("Inativo"),
    SUSPENDED("Suspenso");

    private final String displayName;

    UserStatus(String displayName) {
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