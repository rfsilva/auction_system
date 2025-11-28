package com.leilao.shared.enums;

/**
 * Status possíveis para um Lote
 */
public enum LoteStatus {
    DRAFT("Rascunho", "Lote em elaboração"),
    ACTIVE("Ativo", "Lote ativo e recebendo lances"),
    CLOSED("Fechado", "Lote encerrado"),
    CANCELLED("Cancelado", "Lote cancelado");

    private final String displayName;
    private final String description;

    LoteStatus(String displayName, String description) {
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

    /**
     * Verifica se o status permite edição
     */
    public boolean permiteEdicao() {
        return this == DRAFT;
    }

    /**
     * Verifica se o status é um estado final
     */
    public boolean isEstadoFinal() {
        return this == CLOSED || this == CANCELLED;
    }

    /**
     * Verifica se o lote está visível publicamente
     */
    public boolean isVisivelPublicamente() {
        return this == ACTIVE || this == CLOSED;
    }
}