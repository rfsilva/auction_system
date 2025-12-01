package com.leilao.modules.vendedor.service;

import com.leilao.modules.auth.entity.Usuario;
import com.leilao.modules.auth.repository.UsuarioRepository;
import com.leilao.modules.contrato.repository.ContratoRepository;
import com.leilao.modules.vendedor.dto.VendedorDto;
import com.leilao.modules.vendedor.entity.Vendedor;
import com.leilao.modules.vendedor.repository.VendedorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service para operações com vendedores
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class VendedorService {

    private final VendedorRepository vendedorRepository;
    private final UsuarioRepository usuarioRepository;
    private final ContratoRepository contratoRepository;

    /**
     * Lista todos os vendedores com paginação
     */
    public Page<VendedorDto> listarTodos(Pageable pageable) {
        Page<Vendedor> vendedores = vendedorRepository.findAll(pageable);
        return vendedores.map(this::convertToDto);
    }

    /**
     * Lista apenas vendedores ativos (para seleção em formulários)
     */
    public List<VendedorDto> listarAtivos() {
        List<Vendedor> vendedores = vendedorRepository.findByActiveTrue();
        return vendedores.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Busca vendedor por ID
     */
    public VendedorDto buscarPorId(String vendedorId) {
        Vendedor vendedor = vendedorRepository.findById(vendedorId)
                .orElseThrow(() -> new EntityNotFoundException("Vendedor não encontrado"));
        return convertToDto(vendedor);
    }

    /**
     * Busca vendedor por usuário ID
     */
    public String obterVendedorIdPorUsuarioId(String usuarioId) {
        return vendedorRepository.findByUsuarioId(usuarioId)
                .map(Vendedor::getId)
                .orElseThrow(() -> new EntityNotFoundException("Vendedor não encontrado para o usuário"));
    }

    /**
     * Verifica se usuário é vendedor
     */
    public boolean isVendedor(String usuarioId) {
        return vendedorRepository.existsByUsuarioId(usuarioId);
    }

    /**
     * Busca vendedor por usuário ID
     */
    public Vendedor buscarPorUsuarioId(String usuarioId) {
        return vendedorRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Vendedor não encontrado para o usuário"));
    }

    /**
     * Converte entidade para DTO
     */
    private VendedorDto convertToDto(Vendedor vendedor) {
        VendedorDto dto = new VendedorDto();
        dto.setId(vendedor.getId());
        dto.setUsuarioId(vendedor.getUsuarioId());
        dto.setCompanyName(vendedor.getCompanyName());
        dto.setTaxId(vendedor.getTaxId());
        dto.setContactEmail(vendedor.getContactEmail());
        dto.setContactPhone(vendedor.getContactPhone());
        dto.setDescription(vendedor.getDescription());
        dto.setActive(vendedor.getActive());
        dto.setVerificado(vendedor.getVerificado());
        dto.setCreatedAt(vendedor.getCreatedAt());
        dto.setUpdatedAt(vendedor.getUpdatedAt());

        // Buscar nome do usuário
        try {
            Usuario usuario = usuarioRepository.findById(vendedor.getUsuarioId()).orElse(null);
            if (usuario != null) {
                dto.setUsuarioNome(usuario.getNome());
            }
        } catch (Exception e) {
            log.warn("Erro ao buscar usuário para vendedor: {}", vendedor.getId(), e);
        }

        // Verificar se tem contrato ativo
        try {
            long contratosAtivos = contratoRepository.countContratosAtivos(vendedor.getId(), LocalDateTime.now());
            dto.setTemContratoAtivo(contratosAtivos > 0);
            
            long totalContratos = contratoRepository.countBySellerId(vendedor.getId());
            dto.setTotalContratos((int) totalContratos);
        } catch (Exception e) {
            log.warn("Erro ao buscar contratos para vendedor: {}", vendedor.getId(), e);
            dto.setTemContratoAtivo(false);
            dto.setTotalContratos(0);
        }

        return dto;
    }
}