package com.leilao.modules.vendedor.repository;

import com.leilao.modules.vendedor.entity.Vendedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para operações com Vendedor
 */
@Repository
public interface VendedorRepository extends JpaRepository<Vendedor, String> {

    /**
     * Busca vendedor por ID do usuário
     */
    Optional<Vendedor> findByUsuarioId(String usuarioId);

    /**
     * Verifica se existe vendedor para o usuário
     */
    boolean existsByUsuarioId(String usuarioId);

    /**
     * Busca vendedores ativos
     */
    List<Vendedor> findByActiveTrue();

    /**
     * Busca vendedores por status ativo
     */
    List<Vendedor> findByActive(Boolean active);
}