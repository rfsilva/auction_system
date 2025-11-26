package com.leilao.shared.enums;

/**
 * Status possíveis para um produto no sistema de leilão
 */
public enum ProdutoStatus {
    DRAFT("Rascunho"),
    PENDING_APPROVAL("Aguardando Aprovação"),
    ACTIVE("Ativo"),
    SOLD("Vendido"),
    CANCELLED("Cancelado"),
    EXPIRED("Expirado");

    private final String displayName;

    ProdutoStatus(String displayName) {
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