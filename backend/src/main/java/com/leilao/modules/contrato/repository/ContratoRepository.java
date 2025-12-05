package com.leilao.modules.contrato.repository;

import com.leilao.modules.contrato.entity.Contrato;
import com.leilao.shared.enums.ContractStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository para operações com contratos
 */
@Repository
public interface ContratoRepository extends JpaRepository<Contrato, String> {

    /**
     * Busca contratos por vendedor
     */
    List<Contrato> findBySellerId(String sellerId);

    /**
     * Busca contratos por vendedor com paginação
     */
    Page<Contrato> findBySellerId(String sellerId, Pageable pageable);

    /**
     * Busca contratos por status
     */
    List<Contrato> findByStatus(ContractStatus status);

    /**
     * Busca contratos por status com paginação
     */
    Page<Contrato> findByStatus(ContractStatus status, Pageable pageable);

    /**
     * Busca contratos ativos por vendedor
     */
    @Query("SELECT c FROM Contrato c WHERE c.sellerId = :sellerId AND c.active = true AND c.status = 'ACTIVE' " +
           "AND (c.validFrom IS NULL OR c.validFrom <= :now) " +
           "AND (c.validTo IS NULL OR c.validTo > :now)")
    List<Contrato> findContratosAtivos(@Param("sellerId") String sellerId, @Param("now") LocalDateTime now);

    /**
     * Busca contrato ativo por vendedor e categoria
     */
    @Query("SELECT c FROM Contrato c WHERE c.sellerId = :sellerId AND c.active = true AND c.status = 'ACTIVE' " +
           "AND (c.validFrom IS NULL OR c.validFrom <= :now) " +
           "AND (c.validTo IS NULL OR c.validTo > :now) " +
           "AND (c.categoria IS NULL OR c.categoria = :categoria) " +
           "ORDER BY c.createdAt DESC")
    Optional<Contrato> findContratoAtivoParaCategoria(@Param("sellerId") String sellerId, 
                                                      @Param("categoria") String categoria, 
                                                      @Param("now") LocalDateTime now);

    /**
     * Busca contratos que expiram em um período
     */
    @Query("SELECT c FROM Contrato c WHERE c.status = 'ACTIVE' AND c.validTo BETWEEN :inicio AND :fim")
    List<Contrato> findContratosExpirandoEntre(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    /**
     * Busca contratos expirados que ainda não foram marcados como expirados
     */
    @Query("SELECT c FROM Contrato c WHERE c.status = 'ACTIVE' AND c.validTo < :now")
    List<Contrato> findContratosExpirados(@Param("now") LocalDateTime now);

    /**
     * Verifica se existe contrato ativo conflitante
     */
    @Query("SELECT COUNT(c) > 0 FROM Contrato c WHERE c.sellerId = :sellerId AND c.active = true AND c.status = 'ACTIVE' " +
           "AND (:contratoId IS NULL OR c.id != :contratoId) " +
           "AND (c.validFrom IS NULL OR c.validFrom <= :validTo) " +
           "AND (c.validTo IS NULL OR c.validTo >= :validFrom) " +
           "AND (c.categoria = :categoria OR c.categoria IS NULL OR :categoria IS NULL)")
    boolean existsContratoConflitante(@Param("sellerId") String sellerId,
                                      @Param("contratoId") String contratoId,
                                      @Param("categoria") String categoria,
                                      @Param("validFrom") LocalDateTime validFrom,
                                      @Param("validTo") LocalDateTime validTo);

    /**
     * Busca contratos com filtros combinados
     */
    @Query("SELECT c FROM Contrato c WHERE " +
           "(:sellerId IS NULL OR c.sellerId = :sellerId) " +
           "AND (:status IS NULL OR c.status = :status) " +
           "AND (:categoria IS NULL OR c.categoria = :categoria) " +
           "AND (:active IS NULL OR c.active = :active) " +
           "AND (:feeRateMin IS NULL OR c.feeRate >= :feeRateMin) " +
           "AND (:feeRateMax IS NULL OR c.feeRate <= :feeRateMax) " +
           "AND (:validFromStart IS NULL OR c.validFrom >= :validFromStart) " +
           "AND (:validFromEnd IS NULL OR c.validFrom <= :validFromEnd) " +
           "AND (:validToStart IS NULL OR c.validTo >= :validToStart) " +
           "AND (:validToEnd IS NULL OR c.validTo <= :validToEnd) " +
           "AND (:termo IS NULL OR LOWER(c.terms) LIKE LOWER(CONCAT('%', :termo, '%')))")
    Page<Contrato> findContratosComFiltros(@Param("sellerId") String sellerId,
                                           @Param("status") ContractStatus status,
                                           @Param("categoria") String categoria,
                                           @Param("active") Boolean active,
                                           @Param("feeRateMin") BigDecimal feeRateMin,
                                           @Param("feeRateMax") BigDecimal feeRateMax,
                                           @Param("validFromStart") LocalDateTime validFromStart,
                                           @Param("validFromEnd") LocalDateTime validFromEnd,
                                           @Param("validToStart") LocalDateTime validToStart,
                                           @Param("validToEnd") LocalDateTime validToEnd,
                                           @Param("termo") String termo,
                                           Pageable pageable);

    /**
     * Conta contratos por status
     */
    long countByStatus(ContractStatus status);

    /**
     * Conta contratos por vendedor
     */
    long countBySellerId(String sellerId);

    /**
     * Conta contratos ativos por vendedor
     */
    @Query("SELECT COUNT(c) FROM Contrato c WHERE c.sellerId = :sellerId AND c.active = true AND c.status = 'ACTIVE' " +
           "AND (c.validFrom IS NULL OR c.validFrom <= :now) " +
           "AND (c.validTo IS NULL OR c.validTo > :now)")
    long countContratosAtivos(@Param("sellerId") String sellerId, @Param("now") LocalDateTime now);

    /**
     * Busca categorias distintas dos contratos
     */
    @Query("SELECT DISTINCT c.categoria FROM Contrato c WHERE c.categoria IS NOT NULL ORDER BY c.categoria")
    List<String> findCategoriasDistintas();

    /**
     * Estatísticas de contratos por status
     */
    @Query("SELECT c.status, COUNT(c) FROM Contrato c GROUP BY c.status")
    List<Object[]> getEstatisticasPorStatus();

    /**
     * Busca contratos criados após uma data
     */
    List<Contrato> findByCreatedAtAfter(LocalDateTime data);

    /**
     * Busca contratos por faixa de taxa
     */
    @Query("SELECT c FROM Contrato c WHERE c.feeRate BETWEEN :min AND :max")
    List<Contrato> findByFeeRateRange(@Param("min") BigDecimal min, @Param("max") BigDecimal max);
}