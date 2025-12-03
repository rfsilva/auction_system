package com.leilao.shared.enums;

/**
 * Status possíveis para um lote
 */
public enum LoteStatus {
    DRAFT("status.draft"),
    ACTIVE("status.active"),
    CLOSED("status.closed"),
    CANCELLED("status.cancelled");

    private final String messageKey;

    LoteStatus(String messageKey) {
        this.messageKey = messageKey;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public String getDisplayName() {
        // Retorna a chave para ser resolvida pelo MessageSourceAccessor
        return messageKey;
    }

    public String getDescription() {
        return messageKey;
    }

    @Override
    public String toString() {
        return messageKey;
    }

    public boolean isEstadoFinal() {
        return this == CLOSED || this == CANCELLED;
    }

    public boolean permiteEdicao() {
        return this == DRAFT;
    }

    public boolean isVisivelPublicamente() {
        return this == ACTIVE || this == CLOSED;
    }
}