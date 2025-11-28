package com.leilao.modules.vendedor.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidade Vendedor
 */
@Entity
@Table(name = "tb_vendedor")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vendedor {

    @Id
    @Column(name = "id", columnDefinition = "VARCHAR(36)")
    private String id;

    @Column(name = "usuario_id", nullable = false, unique = true, columnDefinition = "VARCHAR(36)")
    private String usuarioId;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "tax_id", unique = true)
    private String taxId;

    @Column(name = "contract_id", columnDefinition = "VARCHAR(36)")
    private String contractId;

    @Column(name = "fee_rate", nullable = false, precision = 5, scale = 4)
    private BigDecimal feeRate = BigDecimal.valueOf(0.05);

    @Column(name = "documents", columnDefinition = "TEXT")
    private String documents; // JSON como TEXT

    @Column(name = "verificado", nullable = false)
    private Boolean verificado = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = java.util.UUID.randomUUID().toString();
        }
    }
}