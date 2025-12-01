package com.leilao.modules.contrato.service;

import com.leilao.modules.auth.entity.Usuario;
import com.leilao.modules.auth.repository.UsuarioRepository;
import com.leilao.modules.contrato.dto.*;
import com.leilao.modules.contrato.entity.Contrato;
import com.leilao.modules.contrato.repository.ContratoRepository;
import com.leilao.modules.vendedor.entity.Vendedor;
import com.leilao.modules.vendedor.repository.VendedorRepository;
import com.leilao.shared.enums.ContractStatus;
import com.leilao.shared.enums.UserRole;
import com.leilao.shared.exception.BusinessException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Service para operações com contratos
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ContratoService {

    private final ContratoRepository contratoRepository;
    private final VendedorRepository vendedorRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     * Cria um novo contrato
     */
    @Transactional
    public ContratoDto criarContrato(ContratoCreateRequest request, String adminId) {
        log.info("Criando contrato para vendedor: {}", request.getSellerId());

        // Validar se o vendedor existe
        Vendedor vendedor = vendedorRepository.findById(request.getSellerId())
                .orElseThrow(() -> new EntityNotFoundException("Vendedor não encontrado"));

        // Validar se não há conflito com contratos existentes
        validarConflitosContrato(request.getSellerId(), null, request.getCategoria(), 
                                request.getValidFrom(), request.getValidTo());

        // Validar regras de negócio
        validarCriacaoContrato(request);

        // Criar contrato
        Contrato contrato = new Contrato();
        contrato.setSellerId(request.getSellerId());
        contrato.setFeeRate(request.getFeeRate());
        contrato.setTerms(request.getTerms());
        contrato.setValidFrom(request.getValidFrom() != null ? request.getValidFrom() : LocalDateTime.now());
        contrato.setValidTo(request.getValidTo());
        contrato.setCategoria(request.getCategoria());
        contrato.setStatus(ContractStatus.DRAFT);
        contrato.setActive(false);
        contrato.setCreatedBy(adminId);

        contrato = contratoRepository.save(contrato);
        log.info("Contrato criado com sucesso: {}", contrato.getId());

        return convertToDto(contrato);
    }

    /**
     * Atualiza um contrato existente
     */
    @Transactional
    public ContratoDto atualizarContrato(String contratoId, ContratoUpdateRequest request, String adminId) {
        log.info("Atualizando contrato: {}", contratoId);

        Contrato contrato = buscarContratoPorId(contratoId);

        // Validar se pode ser editado
        validarEdicaoContrato(contrato);

        // Validar conflitos se houver mudança de datas ou categoria
        if (request.getValidFrom() != null || request.getValidTo() != null || request.getCategoria() != null) {
            LocalDateTime validFrom = request.getValidFrom() != null ? request.getValidFrom() : contrato.getValidFrom();
            LocalDateTime validTo = request.getValidTo() != null ? request.getValidTo() : contrato.getValidTo();
            String categoria = request.getCategoria() != null ? request.getCategoria() : contrato.getCategoria();
            
            validarConflitosContrato(contrato.getSellerId(), contratoId, categoria, validFrom, validTo);
        }

        // Atualizar campos
        if (request.getFeeRate() != null) {
            contrato.setFeeRate(request.getFeeRate());
        }
        if (request.getTerms() != null) {
            contrato.setTerms(request.getTerms());
        }
        if (request.getValidFrom() != null) {
            contrato.setValidFrom(request.getValidFrom());
        }
        if (request.getValidTo() != null) {
            contrato.setValidTo(request.getValidTo());
        }
        if (request.getCategoria() != null) {
            contrato.setCategoria(request.getCategoria());
        }

        contrato = contratoRepository.save(contrato);
        log.info("Contrato atualizado com sucesso: {}", contratoId);

        return convertToDto(contrato);
    }

    /**
     * Busca contrato por ID
     */
    public ContratoDto buscarPorId(String contratoId) {
        Contrato contrato = buscarContratoPorId(contratoId);
        return convertToDto(contrato);
    }

    /**
     * Lista contratos com filtros
     */
    public Page<ContratoDto> listarContratos(ContratoFiltroRequest filtros) {
        Pageable pageable = criarPageable(filtros);
        
        Page<Contrato> contratos = contratoRepository.findContratosComFiltros(
                filtros.getSellerId(),
                filtros.getStatus(),
                filtros.getCategoria(),
                filtros.getActive(),
                filtros.getFeeRateMin(),
                filtros.getFeeRateMax(),
                filtros.getValidFromStart(),
                filtros.getValidFromEnd(),
                filtros.getValidToStart(),
                filtros.getValidToEnd(),
                filtros.getTermo(),
                pageable
        );

        return contratos.map(this::convertToDto);
    }

    /**
     * Lista contratos por vendedor
     */
    public Page<ContratoDto> listarContratosPorVendedor(String sellerId, Pageable pageable) {
        Page<Contrato> contratos = contratoRepository.findBySellerId(sellerId, pageable);
        return contratos.map(this::convertToDto);
    }

    /**
     * Ativa um contrato
     */
    @Transactional
    public ContratoDto ativarContrato(String contratoId, String adminId) {
        log.info("Ativando contrato: {}", contratoId);

        Contrato contrato = buscarContratoPorId(contratoId);
        contrato.approve(adminId);
        
        contrato = contratoRepository.save(contrato);
        
        // Atualizar role do vendedor se necessário
        atualizarRoleVendedor(contrato.getSellerId());
        
        log.info("Contrato ativado com sucesso: {}", contratoId);
        return convertToDto(contrato);
    }

    /**
     * Cancela um contrato
     */
    @Transactional
    public ContratoDto cancelarContrato(String contratoId, String adminId) {
        log.info("Cancelando contrato: {}", contratoId);

        Contrato contrato = buscarContratoPorId(contratoId);
        contrato.cancel();
        
        contrato = contratoRepository.save(contrato);
        
        // Verificar se vendedor ainda tem contratos ativos
        atualizarRoleVendedor(contrato.getSellerId());
        
        log.info("Contrato cancelado com sucesso: {}", contratoId);
        return convertToDto(contrato);
    }

    /**
     * Suspende um contrato
     */
    @Transactional
    public ContratoDto suspenderContrato(String contratoId, String adminId) {
        log.info("Suspendendo contrato: {}", contratoId);

        Contrato contrato = buscarContratoPorId(contratoId);
        contrato.suspend();
        
        contrato = contratoRepository.save(contrato);
        
        // Verificar se vendedor ainda tem contratos ativos
        atualizarRoleVendedor(contrato.getSellerId());
        
        log.info("Contrato suspenso com sucesso: {}", contratoId);
        return convertToDto(contrato);
    }

    /**
     * Exclui um contrato (apenas rascunhos)
     */
    @Transactional
    public void excluirContrato(String contratoId, String adminId) {
        log.info("Excluindo contrato: {}", contratoId);

        Contrato contrato = buscarContratoPorId(contratoId);
        
        if (contrato.getStatus() != ContractStatus.DRAFT) {
            throw new BusinessException("Apenas contratos em rascunho podem ser excluídos");
        }

        contratoRepository.delete(contrato);
        log.info("Contrato excluído com sucesso: {}", contratoId);
    }

    /**
     * Busca contrato ativo para vendedor e categoria
     */
    public ContratoDto buscarContratoAtivoParaCategoria(String sellerId, String categoria) {
        return contratoRepository.findContratoAtivoParaCategoria(sellerId, categoria, LocalDateTime.now())
                .map(this::convertToDto)
                .orElse(null);
    }

    /**
     * Verifica se vendedor tem contrato ativo
     */
    public boolean vendedorTemContratoAtivo(String sellerId) {
        return contratoRepository.countContratosAtivos(sellerId, LocalDateTime.now()) > 0;
    }

    /**
     * Atualiza contratos expirados
     */
    @Transactional
    public void atualizarContratosExpirados() {
        List<Contrato> contratosExpirados = contratoRepository.findContratosExpirados(LocalDateTime.now());
        
        for (Contrato contrato : contratosExpirados) {
            contrato.expire();
            contratoRepository.save(contrato);
            
            // Verificar se vendedor ainda tem contratos ativos
            atualizarRoleVendedor(contrato.getSellerId());
            
            log.info("Contrato expirado automaticamente: {}", contrato.getId());
        }
    }

    /**
     * Lista categorias disponíveis
     */
    public List<String> listarCategorias() {
        return contratoRepository.findCategoriasDistintas();
    }

    // Métodos privados

    private Contrato buscarContratoPorId(String contratoId) {
        return contratoRepository.findById(contratoId)
                .orElseThrow(() -> new EntityNotFoundException("Contrato não encontrado"));
    }

    private void validarCriacaoContrato(ContratoCreateRequest request) {
        // Validar taxa
        if (request.getFeeRate().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Taxa deve ser maior que zero");
        }

        // Validar datas
        if (request.getValidTo() != null && request.getValidFrom() != null) {
            if (request.getValidTo().isBefore(request.getValidFrom())) {
                throw new BusinessException("Data de fim deve ser posterior à data de início");
            }
        }
    }

    private void validarEdicaoContrato(Contrato contrato) {
        if (!contrato.canBeEdited()) {
            throw new BusinessException("Contrato não pode ser editado no status atual: " + contrato.getStatus());
        }
    }

    private void validarConflitosContrato(String sellerId, String contratoId, String categoria, 
                                        LocalDateTime validFrom, LocalDateTime validTo) {
        if (contratoRepository.existsContratoConflitante(sellerId, contratoId, categoria, validFrom, validTo)) {
            throw new BusinessException("Já existe um contrato ativo conflitante para este vendedor e categoria no período especificado");
        }
    }

    private void atualizarRoleVendedor(String sellerId) {
        try {
            Vendedor vendedor = vendedorRepository.findById(sellerId)
                    .orElseThrow(() -> new EntityNotFoundException("Vendedor não encontrado"));

            Usuario usuario = usuarioRepository.findById(vendedor.getUsuarioId())
                    .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

            boolean temContratoAtivo = vendedorTemContratoAtivo(sellerId);

            if (temContratoAtivo && !usuario.hasRole(UserRole.SELLER)) {
                usuario.addRole(UserRole.SELLER);
                usuarioRepository.save(usuario);
                log.info("Role SELLER adicionada para usuário: {}", usuario.getId());
            } else if (!temContratoAtivo && usuario.hasRole(UserRole.SELLER)) {
                usuario.removeRole(UserRole.SELLER);
                usuarioRepository.save(usuario);
                log.info("Role SELLER removida para usuário: {}", usuario.getId());
            }
        } catch (Exception e) {
            log.error("Erro ao atualizar role do vendedor: {}", sellerId, e);
        }
    }

    private Pageable criarPageable(ContratoFiltroRequest filtros) {
        Sort.Direction direction = "desc".equalsIgnoreCase(filtros.getDirection()) 
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, filtros.getSort());
        return PageRequest.of(filtros.getPage(), filtros.getSize(), sort);
    }

    private ContratoDto convertToDto(Contrato contrato) {
        ContratoDto dto = new ContratoDto();
        dto.setId(contrato.getId());
        dto.setSellerId(contrato.getSellerId());
        dto.setFeeRate(contrato.getFeeRate());
        dto.setTerms(contrato.getTerms());
        dto.setValidFrom(contrato.getValidFrom());
        dto.setValidTo(contrato.getValidTo());
        dto.setActive(contrato.getActive());
        dto.setCategoria(contrato.getCategoria());
        dto.setStatus(contrato.getStatus());
        dto.setCreatedBy(contrato.getCreatedBy());
        dto.setApprovedBy(contrato.getApprovedBy());
        dto.setApprovedAt(contrato.getApprovedAt());
        dto.setCreatedAt(contrato.getCreatedAt());
        dto.setUpdatedAt(contrato.getUpdatedAt());

        // Campos calculados
        dto.setIsActive(contrato.isActive());
        dto.setIsExpired(contrato.isExpired());
        dto.setCanBeActivated(contrato.canBeActivated());
        dto.setCanBeCancelled(contrato.canBeCancelled());
        dto.setCanBeEdited(contrato.canBeEdited());
        dto.setStatusDisplayName(contrato.getStatus().getDisplayName());

        // Calcular dias até expiração
        if (contrato.getValidTo() != null) {
            long days = ChronoUnit.DAYS.between(LocalDateTime.now(), contrato.getValidTo());
            dto.setDaysUntilExpiration(Math.max(0, days));
        }

        // Buscar nomes (opcional - pode ser otimizado com joins)
        try {
            Vendedor vendedor = vendedorRepository.findById(contrato.getSellerId()).orElse(null);
            if (vendedor != null) {
                dto.setSellerCompanyName(vendedor.getCompanyName());
                Usuario usuario = usuarioRepository.findById(vendedor.getUsuarioId()).orElse(null);
                if (usuario != null) {
                    dto.setSellerName(usuario.getNome());
                }
            }

            if (contrato.getCreatedBy() != null) {
                Usuario createdBy = usuarioRepository.findById(contrato.getCreatedBy()).orElse(null);
                if (createdBy != null) {
                    dto.setCreatedByName(createdBy.getNome());
                }
            }

            if (contrato.getApprovedBy() != null) {
                Usuario approvedBy = usuarioRepository.findById(contrato.getApprovedBy()).orElse(null);
                if (approvedBy != null) {
                    dto.setApprovedByName(approvedBy.getNome());
                }
            }
        } catch (Exception e) {
            log.warn("Erro ao buscar nomes para contrato: {}", contrato.getId(), e);
        }

        return dto;
    }
}