package com.leilao.modules.produto.repository;

import com.leilao.modules.produto.entity.Produto;
import com.leilao.shared.enums.ProdutoStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository para operações com Produto
 */
@Repository
public interface ProdutoRepository extends JpaRepository<Produto, String> {

    /**
     * Busca produtos por vendedor
     */
    List<Produto> findBySellerId(String sellerId);

    /**
     * Busca produtos por vendedor com paginação
     */
    Page<Produto> findBySellerId(String sellerId, Pageable pageable);

    /**
     * Busca produtos por status
     */
    List<Produto> findByStatus(ProdutoStatus status);

    /**
     * Busca produtos por status com paginação
     */
    Page<Produto> findByStatus(ProdutoStatus status, Pageable pageable);

    /**
     * Busca produtos por categoria
     */
    List<Produto> findByCategoria(String categoria);

    /**
     * Busca produtos por categoria com paginação
     */
    Page<Produto> findByCategoria(String categoria, Pageable pageable);

    /**
     * Busca produtos ativos (para catálogo público)
     */
    @Query("SELECT p FROM Produto p WHERE p.status = 'ACTIVE' AND p.endDateTime > :now")
    List<Produto> findProdutosAtivos(@Param("now") LocalDateTime now);

    /**
     * Busca produtos ativos com paginação (para catálogo público)
     */
    @Query("SELECT p FROM Produto p WHERE p.status = 'ACTIVE' AND p.endDateTime > :now")
    Page<Produto> findProdutosAtivos(@Param("now") LocalDateTime now, Pageable pageable);

    /**
     * Busca produtos por faixa de preço
     */
    @Query("SELECT p FROM Produto p WHERE p.currentPrice BETWEEN :precoMin AND :precoMax")
    List<Produto> findByFaixaPreco(@Param("precoMin") BigDecimal precoMin, @Param("precoMax") BigDecimal precoMax);

    /**
     * Busca produtos por faixa de preço com paginação
     */
    @Query("SELECT p FROM Produto p WHERE p.currentPrice BETWEEN :precoMin AND :precoMax")
    Page<Produto> findByFaixaPreco(@Param("precoMin") BigDecimal precoMin, @Param("precoMax") BigDecimal precoMax, Pageable pageable);

    /**
     * Busca produtos por título (busca parcial)
     */
    @Query("SELECT p FROM Produto p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :titulo, '%'))")
    List<Produto> findByTituloContaining(@Param("titulo") String titulo);

    /**
     * Busca produtos por título com paginação
     */
    @Query("SELECT p FROM Produto p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :titulo, '%'))")
    Page<Produto> findByTituloContaining(@Param("titulo") String titulo, Pageable pageable);

    /**
     * Busca produtos que terminam em breve
     */
    @Query("SELECT p FROM Produto p WHERE p.status = 'ACTIVE' AND p.endDateTime BETWEEN :now AND :limite")
    List<Produto> findProdutosTerminandoEm(@Param("now") LocalDateTime now, @Param("limite") LocalDateTime limite);

    /**
     * Busca produtos expirados que ainda não foram marcados como EXPIRED
     */
    @Query("SELECT p FROM Produto p WHERE p.status = 'ACTIVE' AND p.endDateTime < :now")
    List<Produto> findProdutosExpirados(@Param("now") LocalDateTime now);

    /**
     * Busca produtos por lote
     */
    List<Produto> findByLoteId(String loteId);

    /**
     * Busca produtos sem lote (produtos avulsos)
     */
    @Query("SELECT p FROM Produto p WHERE p.loteId IS NULL")
    List<Produto> findProdutosSemLote();

    /**
     * Conta produtos por status
     */
    long countByStatus(ProdutoStatus status);

    /**
     * Conta produtos por vendedor
     */
    long countBySellerId(String sellerId);

    /**
     * Busca produtos para catálogo público com filtros
     */
    @Query("""
        SELECT p FROM Produto p 
        WHERE p.status = 'ACTIVE' 
        AND p.endDateTime > :now
        AND (:categoria IS NULL OR p.categoria = :categoria)
        AND (:precoMin IS NULL OR p.currentPrice >= :precoMin)
        AND (:precoMax IS NULL OR p.currentPrice <= :precoMax)
        AND (:titulo IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :titulo, '%')))
        ORDER BY p.createdAt DESC
        """)
    Page<Produto> findCatalogoPublico(
        @Param("now") LocalDateTime now,
        @Param("categoria") String categoria,
        @Param("precoMin") BigDecimal precoMin,
        @Param("precoMax") BigDecimal precoMax,
        @Param("titulo") String titulo,
        Pageable pageable
    );

    /**
     * Busca categorias distintas dos produtos ativos
     */
    @Query("SELECT DISTINCT p.categoria FROM Produto p WHERE p.status = 'ACTIVE' AND p.categoria IS NOT NULL ORDER BY p.categoria")
    List<String> findCategoriasAtivas();

    /**
     * Estatísticas de produtos por status
     */
    @Query(value = """
        SELECT status, COUNT(*) as total 
        FROM tb_produto 
        GROUP BY status 
        ORDER BY total DESC
        """, nativeQuery = true)
    List<Object[]> getEstatisticasPorStatus();
}