package com.leilao.modules.produto.entity;

import com.leilao.shared.enums.ProdutoStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Entidade Produto para o sistema de leilão
 */
@Entity
@Table(name = "tb_produto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produto {

    @Id
    @Column(name = "id", columnDefinition = "VARCHAR(36)")
    private String id;

    @Column(name = "seller_id", nullable = false, columnDefinition = "VARCHAR(36)")
    private String sellerId;

    @Column(name = "lote_id", columnDefinition = "VARCHAR(36)")
    private String loteId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "images", columnDefinition = "TEXT")
    private String images; // JSON como TEXT

    @Column(name = "weight", precision = 10, scale = 3)
    private BigDecimal weight;

    @Column(name = "dimensions", columnDefinition = "TEXT")
    private String dimensions; // JSON como TEXT

    @Column(name = "initial_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal initialPrice;

    @Column(name = "current_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal currentPrice;

    @Column(name = "reserve_price", precision = 15, scale = 2)
    private BigDecimal reservePrice;

    @Column(name = "increment_min", nullable = false, precision = 15, scale = 2)
    private BigDecimal incrementMin = BigDecimal.ONE;

    @Column(name = "end_datetime", nullable = false)
    private LocalDateTime endDateTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProdutoStatus status = ProdutoStatus.DRAFT;

    @Column(name = "moderated", nullable = false)
    private Boolean moderated = false;

    @Column(name = "moderator_id", columnDefinition = "VARCHAR(36)")
    private String moderatorId;

    @Column(name = "moderated_at")
    private LocalDateTime moderatedAt;

    @Column(name = "anti_snipe_enabled", nullable = false)
    private Boolean antiSnipeEnabled = true;

    @Column(name = "anti_snipe_extension", nullable = false)
    private Integer antiSnipeExtension = 300; // segundos

    @Column(name = "categoria")
    private String categoria;

    @Column(name = "tags", columnDefinition = "TEXT")
    private String tags; // Array como TEXT separado por vírgulas

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
        if (this.currentPrice == null) {
            this.currentPrice = this.initialPrice;
        }
    }

    // Métodos utilitários
    public List<String> getImagesList() {
        if (images == null || images.trim().isEmpty()) {
            return List.of();
        }
        // Assumindo formato JSON simples: ["url1", "url2"]
        return List.of(images.replace("[", "").replace("]", "").replace("\"", "").split(","));
    }

    public void setImagesList(List<String> imagesList) {
        if (imagesList == null || imagesList.isEmpty()) {
            this.images = null;
        } else {
            this.images = "[\"" + String.join("\",\"", imagesList) + "\"]";
        }
    }

    public List<String> getTagsList() {
        if (tags == null || tags.trim().isEmpty()) {
            return List.of();
        }
        return List.of(tags.split(","));
    }

    public void setTagsList(List<String> tagsList) {
        if (tagsList == null || tagsList.isEmpty()) {
            this.tags = null;
        } else {
            this.tags = String.join(",", tagsList);
        }
    }

    public boolean isActive() {
        return ProdutoStatus.ACTIVE.equals(this.status);
    }

    public boolean isDraft() {
        return ProdutoStatus.DRAFT.equals(this.status);
    }

    public boolean isSold() {
        return ProdutoStatus.SOLD.equals(this.status);
    }

    public boolean isExpired() {
        return ProdutoStatus.EXPIRED.equals(this.status) || 
               (this.endDateTime != null && this.endDateTime.isBefore(LocalDateTime.now()));
    }

    public boolean canReceiveBids() {
        return isActive() && !isExpired();
    }
}