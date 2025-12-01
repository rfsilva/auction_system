package com.leilao.shared.enums;

/**
 * Status possíveis para um contrato
 */
public enum ContractStatus {
    DRAFT("Rascunho", "Contrato em elaboração"),
    ACTIVE("Ativo", "Contrato ativo e válido"),
    EXPIRED("Expirado", "Contrato expirado por data"),
    CANCELLED("Cancelado", "Contrato cancelado"),
    SUSPENDED("Suspenso", "Contrato temporariamente suspenso");

    private final String displayName;
    private final String description;

    ContractStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return displayName;
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