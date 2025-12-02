package com.leilao.modules.admin.service;

import com.leilao.modules.admin.dto.AdminUsuarioDto;
import com.leilao.modules.admin.dto.AdminUsuarioUpdateRequest;
import com.leilao.modules.admin.dto.UsuarioSugestaoDto;
import com.leilao.modules.auth.entity.Usuario;
import com.leilao.modules.auth.repository.UsuarioRepository;
import com.leilao.modules.contrato.service.ContratoService;
import com.leilao.modules.vendedor.entity.Vendedor;
import com.leilao.modules.vendedor.repository.VendedorRepository;
import com.leilao.shared.enums.UserRole;
import com.leilao.shared.enums.UserStatus;
import com.leilao.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service para operações administrativas com usuários
 * História 2: Processo de Contratação de Vendedores
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AdminUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final VendedorRepository vendedorRepository;
    private final ContratoService contratoService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Lista todos os usuários com paginação e filtros (para gestão administrativa)
     */
    public Page<AdminUsuarioDto> listarUsuariosAdmin(
            Pageable pageable, String nome, String email, UserStatus status, UserRole role,
            Boolean emailVerificado, Boolean telefoneVerificado, Boolean isVendedor, Boolean temContratoAtivo) {
        
        log.info("Listando usuários admin - página: {}, filtros aplicados", pageable.getPageNumber());

        List<Usuario> todosUsuarios = usuarioRepository.findAll();

        // Aplicar filtros
        List<Usuario> usuariosFiltrados = todosUsuarios.stream()
                .filter(u -> nome == null || u.getNome().toLowerCase().contains(nome.toLowerCase()))
                .filter(u -> email == null || u.getEmail().toLowerCase().contains(email.toLowerCase()))
                .filter(u -> status == null || u.getStatus() == status)
                .filter(u -> role == null || u.getRoles().contains(role))
                .filter(u -> emailVerificado == null || u.getEmailVerificado().equals(emailVerificado))
                .filter(u -> telefoneVerificado == null || u.getTelefoneVerificado().equals(telefoneVerificado))
                .filter(u -> isVendedor == null || u.hasRole(UserRole.SELLER) == isVendedor)
                .collect(Collectors.toList());

        // Converter para DTO e aplicar filtro de contrato ativo
        List<AdminUsuarioDto> usuariosDto = usuariosFiltrados.stream()
                .map(this::convertToAdminDto)
                .filter(dto -> temContratoAtivo == null || dto.getTemContratoAtivo().equals(temContratoAtivo))
                .collect(Collectors.toList());

        // Aplicar paginação manual
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), usuariosDto.size());
        
        List<AdminUsuarioDto> paginatedList = usuariosDto.subList(start, end);
        
        return new PageImpl<>(paginatedList, pageable, usuariosDto.size());
    }

    /**
     * Lista todos os usuários com paginação (para seleção em contratos)
     */
    public Page<UsuarioSugestaoDto> listarUsuarios(Pageable pageable, String termo, String tipo) {
        log.info("Listando usuários - página: {}, tamanho: {}, termo: {}, tipo: {}", 
                pageable.getPageNumber(), pageable.getPageSize(), termo, tipo);

        List<Usuario> todosUsuarios;
        
        if (termo != null && !termo.trim().isEmpty()) {
            // Buscar por termo
            todosUsuarios = usuarioRepository.findByNomeOrEmailContaining(termo.trim());
        } else {
            // Buscar todos
            todosUsuarios = usuarioRepository.findAll();
        }

        // Filtrar apenas usuários ativos
        List<Usuario> usuariosAtivos = todosUsuarios.stream()
                .filter(u -> u.getStatus() == UserStatus.ACTIVE)
                .collect(Collectors.toList());

        // Converter para DTO e aplicar filtros de tipo
        List<UsuarioSugestaoDto> usuariosDto = usuariosAtivos.stream()
                .map(this::convertToSugestaoDto)
                .filter(dto -> aplicarFiltroTipo(dto, tipo))
                .collect(Collectors.toList());

        // Aplicar paginação manual
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), usuariosDto.size());
        
        List<UsuarioSugestaoDto> paginatedList = usuariosDto.subList(start, end);
        
        return new PageImpl<>(paginatedList, pageable, usuariosDto.size());
    }

    /**
     * Aplica filtro por tipo de usuário
     */
    private boolean aplicarFiltroTipo(UsuarioSugestaoDto dto, String tipo) {
        if (tipo == null || tipo.trim().isEmpty()) {
            return true; // Sem filtro
        }

        switch (tipo.toLowerCase()) {
            case "vendedor":
                return dto.getIsVendedor();
            case "nao-vendedor":
                return !dto.getIsVendedor();
            case "com-contrato":
                return dto.getTemContratoAtivo() != null && dto.getTemContratoAtivo();
            default:
                return true;
        }
    }

    /**
     * Busca usuários por termo (nome ou email) para seleção em contratos (método antigo)
     */
    public List<UsuarioSugestaoDto> buscarUsuariosPorTermo(String termo, int limit) {
        log.info("Buscando usuários por termo: {}", termo);

        List<Usuario> usuarios = usuarioRepository.findByNomeOrEmailContaining(termo)
                .stream()
                .filter(u -> u.getStatus() == UserStatus.ACTIVE) // Apenas usuários ativos
                .limit(limit)
                .collect(Collectors.toList());

        return usuarios.stream()
                .map(this::convertToSugestaoDto)
                .collect(Collectors.toList());
    }

    /**
     * Busca usuário por ID
     */
    public AdminUsuarioDto buscarUsuarioPorId(String usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        
        return convertToAdminDto(usuario);
    }

    /**
     * Atualiza dados do usuário
     */
    @Transactional
    public AdminUsuarioDto atualizarUsuario(String usuarioId, AdminUsuarioUpdateRequest request) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        if (request.getNome() != null) {
            usuario.setNome(request.getNome());
        }
        if (request.getEmail() != null) {
            // Verificar se email já existe
            if (!usuario.getEmail().equals(request.getEmail()) && 
                usuarioRepository.existsByEmail(request.getEmail())) {
                throw new BusinessException("Email já está em uso por outro usuário");
            }
            usuario.setEmail(request.getEmail());
        }
        if (request.getTelefone() != null) {
            usuario.setTelefone(request.getTelefone());
        }
        if (request.getStatus() != null) {
            usuario.setStatus(request.getStatus());
        }
        if (request.getRoles() != null) {
            usuario.setRoles(request.getRoles());
        }
        if (request.getEmailVerificado() != null) {
            usuario.setEmailVerificado(request.getEmailVerificado());
        }
        if (request.getTelefoneVerificado() != null) {
            usuario.setTelefoneVerificado(request.getTelefoneVerificado());
        }

        usuario = usuarioRepository.save(usuario);
        log.info("Usuário {} atualizado pelo admin", usuario.getId());

        return convertToAdminDto(usuario);
    }

    /**
     * Ativa usuário
     */
    @Transactional
    public AdminUsuarioDto ativarUsuario(String usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        if (usuario.getStatus() == UserStatus.ACTIVE) {
            throw new BusinessException("Usuário já está ativo");
        }

        usuario.setStatus(UserStatus.ACTIVE);
        usuario = usuarioRepository.save(usuario);
        
        log.info("Usuário {} ativado pelo admin", usuario.getId());
        return convertToAdminDto(usuario);
    }

    /**
     * Desativa usuário
     */
    @Transactional
    public AdminUsuarioDto desativarUsuario(String usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        if (usuario.getStatus() != UserStatus.ACTIVE) {
            throw new BusinessException("Usuário não está ativo");
        }

        usuario.setStatus(UserStatus.INACTIVE);
        usuario = usuarioRepository.save(usuario);
        
        log.info("Usuário {} desativado pelo admin", usuario.getId());
        return convertToAdminDto(usuario);
    }

    /**
     * Suspende usuário (equivalente ao "bloquear")
     */
    @Transactional
    public AdminUsuarioDto bloquearUsuario(String usuarioId, String motivo) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        if (usuario.getStatus() == UserStatus.SUSPENDED) {
            throw new BusinessException("Usuário já está suspenso");
        }

        usuario.setStatus(UserStatus.SUSPENDED);
        // Aqui você pode salvar o motivo em uma tabela de auditoria se necessário
        usuario = usuarioRepository.save(usuario);
        
        log.info("Usuário {} suspenso pelo admin. Motivo: {}", usuario.getId(), motivo);
        return convertToAdminDto(usuario);
    }

    /**
     * Remove suspensão do usuário (equivalente ao "desbloquear")
     */
    @Transactional
    public AdminUsuarioDto desbloquearUsuario(String usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        if (usuario.getStatus() != UserStatus.SUSPENDED) {
            throw new BusinessException("Usuário não está suspenso");
        }

        usuario.setStatus(UserStatus.ACTIVE);
        usuario = usuarioRepository.save(usuario);
        
        log.info("Usuário {} reativado pelo admin (suspensão removida)", usuario.getId());
        return convertToAdminDto(usuario);
    }

    /**
     * Verifica email do usuário
     */
    @Transactional
    public AdminUsuarioDto verificarEmail(String usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        if (usuario.getEmailVerificado()) {
            throw new BusinessException("Email já está verificado");
        }

        usuario.setEmailVerificado(true);
        usuario = usuarioRepository.save(usuario);
        
        log.info("Email do usuário {} verificado pelo admin", usuario.getId());
        return convertToAdminDto(usuario);
    }

    /**
     * Verifica telefone do usuário
     */
    @Transactional
    public AdminUsuarioDto verificarTelefone(String usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        if (usuario.getTelefoneVerificado()) {
            throw new BusinessException("Telefone já está verificado");
        }

        usuario.setTelefoneVerificado(true);
        usuario = usuarioRepository.save(usuario);
        
        log.info("Telefone do usuário {} verificado pelo admin", usuario.getId());
        return convertToAdminDto(usuario);
    }

    /**
     * Adiciona role ao usuário
     */
    @Transactional
    public AdminUsuarioDto adicionarRole(String usuarioId, UserRole role) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        if (usuario.hasRole(role)) {
            throw new BusinessException("Usuário já possui esta role");
        }

        usuario.addRole(role);
        usuario = usuarioRepository.save(usuario);
        
        log.info("Role {} adicionada ao usuário {} pelo admin", role, usuario.getId());
        return convertToAdminDto(usuario);
    }

    /**
     * Remove role do usuário
     */
    @Transactional
    public AdminUsuarioDto removerRole(String usuarioId, UserRole role) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        if (!usuario.hasRole(role)) {
            throw new BusinessException("Usuário não possui esta role");
        }

        usuario.removeRole(role);
        usuario = usuarioRepository.save(usuario);
        
        log.info("Role {} removida do usuário {} pelo admin", role, usuario.getId());
        return convertToAdminDto(usuario);
    }

    /**
     * Redefine senha do usuário
     */
    @Transactional
    public String redefinirSenha(String usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        // Gerar nova senha aleatória
        String novaSenha = gerarSenhaAleatoria();
        usuario.setSenhaHash(passwordEncoder.encode(novaSenha));
        usuarioRepository.save(usuario);
        
        log.info("Senha do usuário {} redefinida pelo admin", usuario.getId());
        return novaSenha;
    }

    /**
     * Obtém estatísticas de usuários
     */
    public Map<String, Object> obterEstatisticas() {
        Map<String, Object> stats = new HashMap<>();
        
        long totalUsuarios = usuarioRepository.count();
        long usuariosAtivos = usuarioRepository.countByStatus(UserStatus.ACTIVE);
        long usuariosInativos = usuarioRepository.countByStatus(UserStatus.INACTIVE);
        long usuariosSuspensos = usuarioRepository.countByStatus(UserStatus.SUSPENDED);
        long usuariosPendentes = usuarioRepository.countByStatus(UserStatus.PENDING_VERIFICATION);
        
        List<Usuario> vendedores = usuarioRepository.findUsuariosByRole(UserRole.SELLER.name());
        long totalVendedores = vendedores.size();
        
        stats.put("totalUsuarios", totalUsuarios);
        stats.put("usuariosAtivos", usuariosAtivos);
        stats.put("usuariosInativos", usuariosInativos);
        stats.put("usuariosSuspensos", usuariosSuspensos);
        stats.put("usuariosPendentes", usuariosPendentes);
        stats.put("totalVendedores", totalVendedores);
        
        return stats;
    }

    /**
     * Verifica se usuário pode ser ativado como vendedor
     */
    public boolean podeSerVendedor(String usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        if (usuario == null || usuario.getStatus() != UserStatus.ACTIVE) {
            return false;
        }

        // Verificar se já tem contrato ativo
        Vendedor vendedor = vendedorRepository.findByUsuarioId(usuarioId).orElse(null);
        if (vendedor != null) {
            return !contratoService.vendedorTemContratoAtivo(vendedor.getId());
        }

        return true; // Usuário pode ser vendedor
    }

    /**
     * Gera senha aleatória
     */
    private String gerarSenhaAleatoria() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder senha = new StringBuilder();
        
        for (int i = 0; i < 8; i++) {
            senha.append(chars.charAt(random.nextInt(chars.length())));
        }
        
        return senha.toString();
    }

    /**
     * Converte Usuario para AdminUsuarioDto
     */
    private AdminUsuarioDto convertToAdminDto(Usuario usuario) {
        AdminUsuarioDto dto = new AdminUsuarioDto();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        dto.setTelefone(usuario.getTelefone());
        dto.setStatus(usuario.getStatus());
        dto.setRoles(usuario.getRoles());
        dto.setEmailVerificado(usuario.getEmailVerificado());
        dto.setTelefoneVerificado(usuario.getTelefoneVerificado());
        dto.setUltimoLogin(usuario.getUltimoLogin());
        dto.setCreatedAt(usuario.getCreatedAt());
        dto.setUpdatedAt(usuario.getUpdatedAt());
        
        // Campos calculados
        dto.setIsActive(usuario.getStatus() == UserStatus.ACTIVE);
        dto.setIsBlocked(usuario.getStatus() == UserStatus.SUSPENDED); // SUSPENDED é o equivalente ao "bloqueado"
        dto.setIsVendedor(usuario.hasRole(UserRole.SELLER));

        // Verificar se tem contrato ativo
        Vendedor vendedor = vendedorRepository.findByUsuarioId(usuario.getId()).orElse(null);
        if (vendedor != null) {
            dto.setTemContratoAtivo(contratoService.vendedorTemContratoAtivo(vendedor.getId()));
            // Aqui você pode adicionar lógica para contar contratos se necessário
            dto.setTotalContratos(0); // Placeholder
        } else {
            dto.setTemContratoAtivo(false);
            dto.setTotalContratos(0);
        }

        return dto;
    }

    /**
     * Converte Usuario para UsuarioSugestaoDto
     */
    public UsuarioSugestaoDto convertToSugestaoDto(Usuario usuario) {
        UsuarioSugestaoDto dto = new UsuarioSugestaoDto();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        dto.setTelefone(usuario.getTelefone());
        dto.setIsVendedor(usuario.hasRole(UserRole.SELLER));

        // Verificar se tem contrato ativo
        Vendedor vendedor = vendedorRepository.findByUsuarioId(usuario.getId()).orElse(null);
        if (vendedor != null) {
            dto.setTemContratoAtivo(contratoService.vendedorTemContratoAtivo(vendedor.getId()));
        } else {
            dto.setTemContratoAtivo(false);
        }

        return dto;
    }
}