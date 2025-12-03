package com.leilao.shared.enums;

/**
 * Roles possíveis para um usuário
 */
public enum UserRole {
    ADMIN("role.admin"),
    SELLER("role.seller"),
    BUYER("role.buyer"),
    PARTICIPANT("role.participant"),
    VISITOR("role.visitor");

    private final String messageKey;

    UserRole(String messageKey) {
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