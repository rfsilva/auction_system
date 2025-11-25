package com.leilao.modules.auth.repository;

import com.leilao.modules.auth.entity.Usuario;
import com.leilao.shared.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositório para operações com usuários
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    /**
     * Busca usuário por email
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Verifica se existe usuário com o email
     */
    boolean existsByEmail(String email);

    /**
     * Busca usuários por status
     */
    List<Usuario> findByStatus(UserStatus status);

    /**
     * Busca usuários criados após uma data específica
     */
    List<Usuario> findByCreatedAtAfter(LocalDateTime date);

    /**
     * Busca usuários com tentativas de login excessivas
     */
    @Query("SELECT u FROM Usuario u WHERE u.tentativasLogin >= :maxTentativas")
    List<Usuario> findUsuariosComTentativasExcessivas(@Param("maxTentativas") int maxTentativas);

    /**
     * Busca usuários bloqueados que já podem ser desbloqueados
     */
    @Query("SELECT u FROM Usuario u WHERE u.bloqueadoAte IS NOT NULL AND u.bloqueadoAte <= :agora")
    List<Usuario> findUsuariosParaDesbloquear(@Param("agora") LocalDateTime agora);

    /**
     * Busca usuários por parte do nome ou email
     */
    @Query("SELECT u FROM Usuario u WHERE LOWER(u.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR LOWER(u.email) LIKE LOWER(CONCAT('%', :termo, '%'))")
    List<Usuario> findByNomeOrEmailContaining(@Param("termo") String termo);

    /**
     * Conta usuários por status
     */
    long countByStatus(UserStatus status);

    /**
     * Busca usuários que não fizeram login há muito tempo
     */
    @Query("SELECT u FROM Usuario u WHERE u.ultimoLogin IS NULL OR u.ultimoLogin < :dataLimite")
    List<Usuario> findUsuariosInativos(@Param("dataLimite") LocalDateTime dataLimite);

    /**
     * Query nativa para buscar usuários com roles específicas
     * Atualizada para usar tb_usuario e tb_usuario_role
     */
    @Query(value = """
        SELECT DISTINCT u.* FROM tb_usuario u 
        INNER JOIN tb_usuario_role ur ON u.id = ur.usuario_id 
        WHERE ur.role = :role
        """, nativeQuery = true)
    List<Usuario> findUsuariosByRole(@Param("role") String role);

    /**
     * Query nativa para estatísticas de usuários por status
     * Atualizada para usar tb_usuario
     */
    @Query(value = """
        SELECT status, COUNT(*) as total 
        FROM tb_usuario 
        GROUP BY status 
        ORDER BY total DESC
        """, nativeQuery = true)
    List<Object[]> getEstatisticasPorStatus();
}