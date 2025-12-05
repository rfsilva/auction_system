package com.leilao.modules.contrato.entity;

import com.leilao.shared.enums.ContractStatus;
import com.leilao.shared.util.MessageUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidade que representa um contrato de vendedor na plataforma com suporte a i18n
 */
@Entity
@Table(name = "tb_contrato")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contrato {

    @Id
    @UuidGenerator
    @Column(name = "id", columnDefinition = "VARCHAR(36)")
    private String id; // String para compatibilidade com VARCHAR(36)

    @Column(name = "seller_id", nullable = false, columnDefinition = "VARCHAR(36)")
    private String sellerId; // String para compatibilidade com VARCHAR(36)

    @Column(name = "fee_rate", nullable = false, precision = 5, scale = 4)
    private BigDecimal feeRate;

    @Column(name = "terms", nullable = false, columnDefinition = "TEXT")
    private String terms;

    @Column(name = "valid_from", nullable = false)
    private LocalDateTime validFrom;

    @Column(name = "valid_to")
    private LocalDateTime validTo;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Campos adicionais para regras de negócio
    @Column(name = "categoria")
    private String categoria; // Categoria específica do contrato (opcional)

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ContractStatus status = ContractStatus.DRAFT;

    @Column(name = "created_by", columnDefinition = "VARCHAR(36)")
    private String createdBy; // Admin que criou o contrato

    @Column(name = "approved_by", columnDefinition = "VARCHAR(36)")
    private String approvedBy; // Admin que aprovou o contrato

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @PrePersist
    protected void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
        if (validFrom == null) {
            validFrom = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Métodos de negócio
    public boolean isActive() {
        LocalDateTime now = LocalDateTime.now();
        return active && 
               status == ContractStatus.ACTIVE &&
               (validFrom == null || !validFrom.isAfter(now)) &&
               (validTo == null || !validTo.isBefore(now));
    }

    public boolean isExpired() {
        LocalDateTime now = LocalDateTime.now();
        return validTo != null && validTo.isBefore(now);
    }

    public boolean canBeActivated() {
        return status == ContractStatus.DRAFT || status == ContractStatus.SUSPENDED;
    }

    public boolean canBeCancelled() {
        return status == ContractStatus.ACTIVE || status == ContractStatus.DRAFT;
    }

    public boolean canBeEdited() {
        return status == ContractStatus.DRAFT;
    }

    // Métodos de negócio com mensagens internacionalizadas
    public void activate() {
        if (!canBeActivated()) {
            throw new IllegalStateException(MessageUtils.getMessage("contract.cannot.activate"));
        }
        this.status = ContractStatus.ACTIVE;
        this.active = true;
    }

    public void cancel() {
        if (!canBeCancelled()) {
            throw new IllegalStateException(MessageUtils.getMessage("contract.cannot.cancel"));
        }
        this.status = ContractStatus.CANCELLED;
        this.active = false;
    }

    public void suspend() {
        if (status != ContractStatus.ACTIVE) {
            throw new IllegalStateException(MessageUtils.getMessage("contract.cannot.suspend"));
        }
        this.status = ContractStatus.SUSPENDED;
        this.active = false;
    }

    public void expire() {
        this.status = ContractStatus.EXPIRED;
        this.active = false;
    }

    public void approve(String approvedBy) {
        if (status != ContractStatus.DRAFT && status != ContractStatus.SUSPENDED) {
            throw new IllegalStateException(MessageUtils.getMessage("contract.cannot.approve"));
        }
        this.status = ContractStatus.ACTIVE;
        this.active = true;
        this.approvedBy = approvedBy;
        this.approvedAt = LocalDateTime.now();
    }

    // Validações de categoria
    public boolean isForCategory(String categoria) {
        return this.categoria == null || this.categoria.equals(categoria);
    }

    public boolean isGeneral() {
        return this.categoria == null || this.categoria.trim().isEmpty();
    }
}