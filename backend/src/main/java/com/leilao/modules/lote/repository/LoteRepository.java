package com.leilao.modules.lote.repository;

import com.leilao.modules.lote.entity.Lote;
import com.leilao.shared.enums.LoteStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository para Lote
 */
@Repository
public interface LoteRepository extends JpaRepository<Lote, String> {

    /**
     * Busca lotes por vendedor
     */
    List<Lote> findBySellerId(String sellerId);

    /**
     * Busca lotes por vendedor com paginação
     */
    Page<Lote> findBySellerId(String sellerId, Pageable pageable);

    /**
     * Busca lotes por status
     */
    List<Lote> findByStatus(LoteStatus status);

    /**
     * Busca lotes por status com paginação
     */
    Page<Lote> findByStatus(LoteStatus status, Pageable pageable);

    /**
     * Busca lotes por vendedor e status
     */
    List<Lote> findBySellerIdAndStatus(String sellerId, LoteStatus status);

    /**
     * Busca lotes por vendedor e status com paginação
     */
    Page<Lote> findBySellerIdAndStatus(String sellerId, LoteStatus status, Pageable pageable);

    /**
     * Busca lotes que encerram em um período específico
     */
    @Query("SELECT l FROM Lote l WHERE l.loteEndDateTime BETWEEN :inicio AND :fim")
    List<Lote> findByLoteEndDateTimeBetween(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    /**
     * Busca lotes que encerram nas próximas horas
     */
    @Query("SELECT l FROM Lote l WHERE l.status = 'ACTIVE' AND l.loteEndDateTime <= :dataLimite")
    List<Lote> findLotesEncerrando(@Param("dataLimite") LocalDateTime dataLimite);

    /**
     * Busca lotes expirados que ainda não foram marcados como fechados
     */
    @Query("SELECT l FROM Lote l WHERE l.status = 'ACTIVE' AND l.loteEndDateTime < :agora")
    List<Lote> findLotesExpirados(@Param("agora") LocalDateTime agora);

    /**
     * Busca lotes por título (busca parcial, case insensitive)
     */
    @Query("SELECT l FROM Lote l WHERE LOWER(l.title) LIKE LOWER(CONCAT('%', :termo, '%'))")
    Page<Lote> findByTitleContaining(@Param("termo") String termo, Pageable pageable);

    /**
     * Busca lotes por título ou descrição (busca parcial, case insensitive)
     */
    @Query("SELECT l FROM Lote l WHERE LOWER(l.title) LIKE LOWER(CONCAT('%', :termo, '%')) " +
           "OR LOWER(l.description) LIKE LOWER(CONCAT('%', :termo, '%'))")
    Page<Lote> findByTitleOrDescriptionContaining(@Param("termo") String termo, Pageable pageable);

    /**
     * Busca lotes públicos (visíveis para visitantes)
     */
    @Query("SELECT l FROM Lote l WHERE l.status IN ('ACTIVE', 'CLOSED')")
    Page<Lote> findLotesPublicos(Pageable pageable);

    /**
     * Busca lotes públicos com filtros combinados
     */
    @Query("SELECT l FROM Lote l WHERE l.status IN ('ACTIVE', 'CLOSED') " +
           "AND (:termo IS NULL OR LOWER(l.title) LIKE LOWER(CONCAT('%', :termo, '%')))")
    Page<Lote> findLotesPublicosComFiltros(@Param("termo") String termo, Pageable pageable);

    /**
     * Conta lotes por status
     */
    long countByStatus(LoteStatus status);

    /**
     * Conta lotes por vendedor
     */
    long countBySellerId(String sellerId);

    /**
     * Conta lotes por vendedor e status
     */
    long countBySellerIdAndStatus(String sellerId, LoteStatus status);

    /**
     * Busca lotes criados após uma data específica
     */
    List<Lote> findByCreatedAtAfter(LocalDateTime data);

    /**
     * Busca lotes atualizados após uma data específica
     */
    List<Lote> findByUpdatedAtAfter(LocalDateTime data);

    /**
     * Verifica se existe lote ativo para um vendedor
     */
    @Query("SELECT COUNT(l) > 0 FROM Lote l WHERE l.sellerId = :sellerId AND l.status = 'ACTIVE'")
    boolean existsLoteAtivoParaVendedor(@Param("sellerId") String sellerId);

    /**
     * Busca lotes ordenados por data de criação
     */
    List<Lote> findBySellerIdOrderByCreatedAtDesc(String sellerId);

    /**
     * Busca lotes ordenados por data de encerramento
     */
    @Query("SELECT l FROM Lote l WHERE l.status = 'ACTIVE' ORDER BY l.loteEndDateTime ASC")
    List<Lote> findLotesAtivosOrdenadosPorEncerramento();
}