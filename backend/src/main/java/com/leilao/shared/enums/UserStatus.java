package com.leilao.shared.enums;

/**
 * Status possíveis para um usuário
 */
public enum UserStatus {
    ACTIVE("status.active"),
    INACTIVE("status.inactive"),
    PENDING_VERIFICATION("status.pending_verification"),  // Corrigido para corresponder ao banco
    SUSPENDED("status.suspended");

    private final String messageKey;

    UserStatus(String messageKey) {
        this.messageKey = messageKey;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public String getDisplayName() {
        // Retorna a chave para ser resolvida pelo MessageSourceAccessor
        return messageKey;
    }

    @Override
    public String toString() {
        return messageKey;
    }
}