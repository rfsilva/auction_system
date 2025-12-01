package com.leilao.modules.admin.service;

import com.leilao.modules.admin.dto.UsuarioSugestaoDto;
import com.leilao.modules.auth.entity.Usuario;
import com.leilao.modules.auth.repository.UsuarioRepository;
import com.leilao.modules.contrato.service.ContratoService;
import com.leilao.modules.vendedor.entity.Vendedor;
import com.leilao.modules.vendedor.repository.VendedorRepository;
import com.leilao.shared.enums.UserRole;
import com.leilao.shared.enums.UserStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
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

    /**
     * Busca usuários por termo (nome ou email) para seleção em contratos
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
     * Converte Usuario para UsuarioSugestaoDto
     */
    private UsuarioSugestaoDto convertToSugestaoDto(Usuario usuario) {
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