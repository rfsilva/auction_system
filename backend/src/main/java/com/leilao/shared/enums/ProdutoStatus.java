package com.leilao.shared.enums;

/**
 * Status possíveis para um produto
 */
public enum ProdutoStatus {
    DRAFT("status.draft"),
    ACTIVE("status.active"),
    SOLD("status.sold"),
    EXPIRED("status.expired"),
    CANCELLED("status.cancelled"),
    PENDING_APPROVAL("status.pending");

    private final String messageKey;

    ProdutoStatus(String messageKey) {
        this.messageKey = messageKey;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public String getDisplayName() {
        // Retorna a chave para ser resolvida pelo MessageSourceAccessor
        return messageKey;
    }

    public String getDescricao() {
        return messageKey;
    }

    @Override
    public String toString() {
        return messageKey;
    }

    public boolean isEstadoFinal() {
        return this == SOLD || this == EXPIRED || this == CANCELLED;
    }

    public boolean permiteEdicao() {
        return this == DRAFT || this == PENDING_APPROVAL;
    }

    public boolean permiteRecebeLances() {
        return this == ACTIVE;
    }

    public boolean isVisivelPublicamente() {
        return this == ACTIVE || this == SOLD || this == EXPIRED;
    }
}