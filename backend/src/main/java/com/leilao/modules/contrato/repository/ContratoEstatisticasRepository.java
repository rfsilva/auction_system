package com.leilao.modules.contrato.repository;

import com.leilao.modules.contrato.entity.Contrato;
import com.leilao.shared.enums.ContractStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository específico para consultas de estatísticas de contratos
 * História 1: Endpoints de Estatísticas de Contratos - Sprint S2.2
 * VERSÃO CORRIGIDA - Fix para parâmetro status
 */
@Repository
public interface ContratoEstatisticasRepository extends JpaRepository<Contrato, String> {

    /**
     * Conta vendedores únicos com contratos ativos
     */
    @Query("SELECT COUNT(DISTINCT c.sellerId) FROM Contrato c WHERE c.active = true AND c.status = 'ACTIVE' " +
           "AND (c.validFrom IS NULL OR c.validFrom <= :now) " +
           "AND (c.validTo IS NULL OR c.validTo > :now)")
    Long countVendedoresAtivos(@Param("now") LocalDateTime now);

    /**
     * Calcula taxa média de comissão dos contratos ativos
     */
    @Query("SELECT AVG(c.feeRate) FROM Contrato c WHERE c.active = true AND c.status = 'ACTIVE' " +
           "AND (c.validFrom IS NULL OR c.validFrom <= :now) " +
           "AND (c.validTo IS NULL OR c.validTo > :now)")
    BigDecimal getMediaTaxaComissaoAtivos(@Param("now") LocalDateTime now);

    /**
     * Conta contratos que vencem nos próximos N dias
     */
    @Query("SELECT COUNT(c) FROM Contrato c WHERE c.status = 'ACTIVE' " +
           "AND c.validTo IS NOT NULL " +
           "AND c.validTo BETWEEN :now AND :dataLimite")
    Long countContratosVencendoEm(@Param("now") LocalDateTime now, @Param("dataLimite") LocalDateTime dataLimite);

    /**
     * Conta contratos criados no período
     */
    @Query("SELECT COUNT(c) FROM Contrato c WHERE c.createdAt BETWEEN :inicio AND :fim")
    Long countContratosCriadosNoPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    /**
     * Conta contratos expirados no período
     */
    @Query("SELECT COUNT(c) FROM Contrato c WHERE c.status = 'EXPIRED' " +
           "AND c.updatedAt BETWEEN :inicio AND :fim")
    Long countContratosExpiradosNoPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    /**
     * Conta categorias distintas com contratos ativos
     */
    @Query("SELECT COUNT(DISTINCT c.categoria) FROM Contrato c WHERE c.active = true AND c.status = 'ACTIVE' " +
           "AND c.categoria IS NOT NULL " +
           "AND (c.validFrom IS NULL OR c.validFrom <= :now) " +
           "AND (c.validTo IS NULL OR c.validTo > :now)")
    Long countCategoriasAtivas(@Param("now") LocalDateTime now);

    /**
     * Busca estatísticas de contratos por status para o período atual
     */
    @Query("SELECT c.status as status, COUNT(c) as total FROM Contrato c GROUP BY c.status")
    List<Object[]> getEstatisticasContratosPorStatus();

    /**
     * Busca contratos ativos para cálculo de receita projetada
     */
    @Query("SELECT c FROM Contrato c WHERE c.active = true AND c.status = 'ACTIVE' " +
           "AND (c.validFrom IS NULL OR c.validFrom <= :now) " +
           "AND (c.validTo IS NULL OR c.validTo > :now)")
    List<Contrato> findContratosAtivosParaReceita(@Param("now") LocalDateTime now);

    /**
     * Busca contratos por vendedor para cálculo de comissões
     */
    @Query("SELECT c FROM Contrato c WHERE c.sellerId = :sellerId " +
           "AND c.createdAt BETWEEN :inicio AND :fim")
    List<Contrato> findContratosPorVendedorNoPeriodo(@Param("sellerId") String sellerId,
                                                     @Param("inicio") LocalDateTime inicio,
                                                     @Param("fim") LocalDateTime fim);

    /**
     * Busca todos os contratos no período para relatório de comissões
     * CORRIGIDO: Agora aceita ContractStatus como enum
     */
    @Query("SELECT c FROM Contrato c WHERE c.createdAt BETWEEN :inicio AND :fim " +
           "AND (:vendedorId IS NULL OR c.sellerId = :vendedorId) " +
           "AND (:categoria IS NULL OR c.categoria = :categoria) " +
           "AND (:status IS NULL OR c.status = :status)")
    List<Contrato> findContratosParaRelatorioComissoes(@Param("inicio") LocalDateTime inicio,
                                                       @Param("fim") LocalDateTime fim,
                                                       @Param("vendedorId") String vendedorId,
                                                       @Param("categoria") String categoria,
                                                       @Param("status") ContractStatus status);
}