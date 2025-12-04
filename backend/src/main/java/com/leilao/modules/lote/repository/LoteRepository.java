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
 * Atualizado para usar contractId ao invés de sellerId
 * História 02: Adicionados métodos para catálogo público de lotes
 */
@Repository
public interface LoteRepository extends JpaRepository<Lote, String> {

    /**
     * Busca lotes por contrato
     */
    List<Lote> findByContractId(String contractId);

    /**
     * Busca lotes por contrato com paginação
     */
    Page<Lote> findByContractId(String contractId, Pageable pageable);

    /**
     * Busca lotes por vendedor (através do contrato)
     */
    @Query("SELECT l FROM Lote l JOIN Contrato c ON l.contractId = c.id WHERE c.sellerId = :sellerId")
    List<Lote> findByVendedorId(@Param("sellerId") String sellerId);

    /**
     * Busca lotes por vendedor com paginação (através do contrato)
     */
    @Query("SELECT l FROM Lote l JOIN Contrato c ON l.contractId = c.id WHERE c.sellerId = :sellerId")
    Page<Lote> findByVendedorId(@Param("sellerId") String sellerId, Pageable pageable);

    /**
     * Busca lotes por status
     */
    List<Lote> findByStatus(LoteStatus status);

    /**
     * Busca lotes por status com paginação
     */
    Page<Lote> findByStatus(LoteStatus status, Pageable pageable);

    /**
     * Busca lotes por contrato e status
     */
    List<Lote> findByContractIdAndStatus(String contractId, LoteStatus status);

    /**
     * Busca lotes por contrato e status com paginação
     */
    Page<Lote> findByContractIdAndStatus(String contractId, LoteStatus status, Pageable pageable);

    /**
     * Busca lotes por vendedor e status (através do contrato)
     */
    @Query("SELECT l FROM Lote l JOIN Contrato c ON l.contractId = c.id WHERE c.sellerId = :sellerId AND l.status = :status")
    List<Lote> findByVendedorIdAndStatus(@Param("sellerId") String sellerId, @Param("status") LoteStatus status);

    /**
     * Busca lotes por vendedor e status com paginação (através do contrato)
     */
    @Query("SELECT l FROM Lote l JOIN Contrato c ON l.contractId = c.id WHERE c.sellerId = :sellerId AND l.status = :status")
    Page<Lote> findByVendedorIdAndStatus(@Param("sellerId") String sellerId, @Param("status") LoteStatus status, Pageable pageable);

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
     * Conta lotes por contrato
     */
    long countByContractId(String contractId);

    /**
     * Conta lotes por vendedor (através do contrato)
     */
    @Query("SELECT COUNT(l) FROM Lote l JOIN Contrato c ON l.contractId = c.id WHERE c.sellerId = :sellerId")
    long countByVendedorId(@Param("sellerId") String sellerId);

    /**
     * Conta lotes por contrato e status
     */
    long countByContractIdAndStatus(String contractId, LoteStatus status);

    /**
     * Conta lotes por vendedor e status (através do contrato)
     */
    @Query("SELECT COUNT(l) FROM Lote l JOIN Contrato c ON l.contractId = c.id WHERE c.sellerId = :sellerId AND l.status = :status")
    long countByVendedorIdAndStatus(@Param("sellerId") String sellerId, @Param("status") LoteStatus status);

    /**
     * Busca lotes criados após uma data específica
     */
    List<Lote> findByCreatedAtAfter(LocalDateTime data);

    /**
     * Busca lotes atualizados após uma data específica
     */
    List<Lote> findByUpdatedAtAfter(LocalDateTime data);

    /**
     * Verifica se existe lote ativo para um contrato específico
     */
    @Query("SELECT COUNT(l) > 0 FROM Lote l WHERE l.contractId = :contractId AND l.status = 'ACTIVE'")
    boolean existsLoteAtivoParaContrato(@Param("contractId") String contractId);

    /**
     * Verifica se existe lote ativo para um vendedor (através do contrato)
     */
    @Query("SELECT COUNT(l) > 0 FROM Lote l JOIN Contrato c ON l.contractId = c.id WHERE c.sellerId = :sellerId AND l.status = 'ACTIVE'")
    boolean existsLoteAtivoParaVendedor(@Param("sellerId") String sellerId);

    /**
     * Busca lotes por contrato ordenados por data de criação
     */
    List<Lote> findByContractIdOrderByCreatedAtDesc(String contractId);

    /**
     * Busca lotes por vendedor ordenados por data de criação (através do contrato)
     */
    @Query("SELECT l FROM Lote l JOIN Contrato c ON l.contractId = c.id WHERE c.sellerId = :sellerId ORDER BY l.createdAt DESC")
    List<Lote> findByVendedorIdOrderByCreatedAtDesc(@Param("sellerId") String sellerId);

    /**
     * Busca lotes ordenados por data de encerramento
     */
    @Query("SELECT l FROM Lote l WHERE l.status = 'ACTIVE' ORDER BY l.loteEndDateTime ASC")
    List<Lote> findLotesAtivosOrdenadosPorEncerramento();

    /**
     * Busca lotes com contratos ativos
     */
    @Query("SELECT l FROM Lote l JOIN Contrato c ON l.contractId = c.id WHERE c.status = 'ACTIVE' AND c.active = true")
    List<Lote> findLotesComContratosAtivos();

    /**
     * Busca lotes por categoria (através do contrato)
     */
    @Query("SELECT l FROM Lote l JOIN Contrato c ON l.contractId = c.id WHERE c.categoria = :categoria")
    List<Lote> findByCategoria(@Param("categoria") String categoria);

    /**
     * Busca lotes por categoria com paginação (através do contrato)
     */
    @Query("SELECT l FROM Lote l JOIN Contrato c ON l.contractId = c.id WHERE c.categoria = :categoria")
    Page<Lote> findByCategoria(@Param("categoria") String categoria, Pageable pageable);

    // ========================================
    // HISTÓRIA 02: Métodos para catálogo público de lotes
    // ========================================

    /**
     * Busca lotes ativos com produtos válidos para catálogo público
     * História 02: Apenas lotes com produtos válidos são exibidos
     */
    @Query("""
        SELECT DISTINCT l FROM Lote l 
        JOIN Produto p ON p.loteId = l.id 
        WHERE l.status = 'ACTIVE' 
        AND l.loteEndDateTime > :now 
        AND p.status IN ('ACTIVE', 'PUBLISHED')
        AND (:termo IS NULL OR LOWER(l.title) LIKE LOWER(CONCAT('%', :termo, '%')) 
             OR LOWER(l.description) LIKE LOWER(CONCAT('%', :termo, '%')))
        AND (:categoria IS NULL OR EXISTS (
            SELECT 1 FROM Contrato c WHERE c.id = l.contractId AND c.categoria = :categoria
        ))
        """)
    Page<Lote> findLotesCatalogoPublico(
        @Param("now") LocalDateTime now,
        @Param("termo") String termo,
        @Param("categoria") String categoria,
        Pageable pageable
    );

    /**
     * Busca lotes encerrando em 1 semana (para destaque)
     * História 02: Lotes em destaque com critério de 1 semana
     */
    @Query("""
        SELECT DISTINCT l FROM Lote l 
        JOIN Produto p ON p.loteId = l.id 
        WHERE l.status = 'ACTIVE' 
        AND l.loteEndDateTime BETWEEN :now AND :oneWeekFromNow 
        AND p.status IN ('ACTIVE', 'PUBLISHED')
        ORDER BY l.loteEndDateTime ASC
        """)
    List<Lote> findLotesEncerrando1Semana(
        @Param("now") LocalDateTime now, 
        @Param("oneWeekFromNow") LocalDateTime oneWeekFromNow
    );

    // Métodos de compatibilidade temporária (manter durante migração)
    /**
     * @deprecated Usar findByVendedorId ao invés
     */
    @Deprecated
    @Query("SELECT l FROM Lote l WHERE l.sellerId = :sellerId")
    List<Lote> findBySellerId(@Param("sellerId") String sellerId);

    /**
     * @deprecated Usar findByVendedorId ao invés
     */
    @Deprecated
    @Query("SELECT l FROM Lote l WHERE l.sellerId = :sellerId")
    Page<Lote> findBySellerId(@Param("sellerId") String sellerId, Pageable pageable);
}