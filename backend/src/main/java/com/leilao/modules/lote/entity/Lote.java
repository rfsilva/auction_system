package com.leilao.modules.lote.entity;

import com.leilao.shared.enums.LoteStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidade Lote - Representa um lote de produtos para leilão
 */
@Entity
@Table(name = "tb_lote")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Lote {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "seller_id", nullable = false, length = 36)
    private String sellerId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "lote_end_datetime", nullable = false)
    private LocalDateTime loteEndDateTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private LoteStatus status = LoteStatus.DRAFT;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void prePersist() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Métodos de negócio
    public boolean isDraft() {
        return LoteStatus.DRAFT.equals(status);
    }

    public boolean isActive() {
        return LoteStatus.ACTIVE.equals(status);
    }

    public boolean isClosed() {
        return LoteStatus.CLOSED.equals(status);
    }

    public boolean isCancelled() {
        return LoteStatus.CANCELLED.equals(status);
    }

    public boolean canBeEdited() {
        return isDraft();
    }

    public boolean canBeActivated() {
        return isDraft() && loteEndDateTime != null && loteEndDateTime.isAfter(LocalDateTime.now());
    }

    public boolean canBeCancelled() {
        return isDraft() || isActive();
    }

    public boolean isExpired() {
        return loteEndDateTime != null && loteEndDateTime.isBefore(LocalDateTime.now());
    }

    public void activate() {
        if (!canBeActivated()) {
            throw new IllegalStateException("Lote não pode ser ativado no status atual: " + status);
        }
        this.status = LoteStatus.ACTIVE;
    }

    public void cancel() {
        if (!canBeCancelled()) {
            throw new IllegalStateException("Lote não pode ser cancelado no status atual: " + status);
        }
        this.status = LoteStatus.CANCELLED;
    }

    public void close() {
        if (!isActive()) {
            throw new IllegalStateException("Apenas lotes ativos podem ser fechados");
        }
        this.status = LoteStatus.CLOSED;
    }
}