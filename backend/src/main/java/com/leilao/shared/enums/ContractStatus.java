package com.leilao.shared.enums;

/**
 * Status possíveis para um contrato
 */
public enum ContractStatus {
    DRAFT("status.draft"),
    ACTIVE("status.active"),
    EXPIRED("status.expired"),
    CANCELLED("status.cancelled"),
    SUSPENDED("status.suspended");

    private final String messageKey;

    ContractStatus(String messageKey) {
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

    public boolean isEstadoFinal() {
        return this == EXPIRED || this == CANCELLED;
    }

    public boolean permiteEdicao() {
        return this == DRAFT;
    }

    public boolean isAtivo() {
        return this == ACTIVE;
    }
}