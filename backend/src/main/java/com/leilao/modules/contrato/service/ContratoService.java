package com.leilao.modules.contrato.service;

import com.leilao.modules.auth.entity.Usuario;
import com.leilao.modules.auth.repository.UsuarioRepository;
import com.leilao.modules.contrato.dto.*;
import com.leilao.modules.contrato.entity.Contrato;
import com.leilao.modules.contrato.repository.ContratoRepository;
import com.leilao.modules.vendedor.entity.Vendedor;
import com.leilao.modules.vendedor.repository.VendedorRepository;
import com.leilao.modules.vendedor.service.VendedorService;
import com.leilao.shared.enums.ContractStatus;
import com.leilao.shared.enums.UserRole;
import com.leilao.shared.enums.UserStatus;
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
import java.util.stream.Collectors;

/**
 * Service para operações com contratos
 * Inclui funcionalidades da História 2: Processo de Contratação de Vendedores
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ContratoService {

    private final ContratoRepository contratoRepository;
    private final VendedorRepository vendedorRepository;
    private final UsuarioRepository usuarioRepository;
    private final VendedorService vendedorService;

    /**
     * Lista contratos ativos do vendedor atual (para seleção em lotes)
     */
    public List<ContratoDto> listarContratosAtivosDoVendedor(String usuarioId) {
        log.info("Listando contratos ativos para usuário: {}", usuarioId);
        
        // Obter ID do vendedor a partir do ID do usuário
        String vendedorId = vendedorService.obterVendedorIdPorUsuarioId(usuarioId);
        
        // Buscar contratos ativos
        List<Contrato> contratos = contratoRepository.findContratosAtivos(vendedorId, LocalDateTime.now());
        
        return contratos.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Cria um novo contrato a partir de usuário (novo fluxo)
     * Promove automaticamente o usuário a vendedor se necessário
     */
    @Transactional
    public ContratoDto criarContratoDeUsuario(ContratoCreateFromUserRequest request, String adminId) {
        log.info("Criando contrato para usuário: {}", request.getUsuarioId());

        // Validar se o usuário existe e está ativo
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        if (usuario.getStatus() != UserStatus.ACTIVE) {
            throw new BusinessException("Usuário deve estar ativo para ter um contrato");
        }

        // Verificar se já é vendedor
        Vendedor vendedor = vendedorRepository.findByUsuarioId(request.getUsuarioId()).orElse(null);
        
        // Se não é vendedor, criar registro de vendedor
        if (vendedor == null) {
            vendedor = criarVendedor(usuario);
            log.info("Usuário {} promovido a vendedor: {}", usuario.getId(), vendedor.getId());
        }

        // Verificar se já tem contrato ativo conflitante
        validarConflitosContrato(vendedor.getId(), null, request.getCategoria(), 
                                request.getValidFrom(), request.getValidTo());

        // Validar regras de negócio
        validarCriacaoContratoFromUser(request);

        // Criar contrato
        Contrato contrato = new Contrato();
        contrato.setSellerId(vendedor.getId());
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

        // Ativar imediatamente se solicitado
        ContratoDto contratoDto = convertToDto(contrato);
        if (Boolean.TRUE.equals(request.getAtivarImediatamente())) {
            contratoDto = ativarContrato(contrato.getId(), adminId);
            log.info("Contrato ativado imediatamente: {}", contrato.getId());
        }

        return contratoDto;
    }

    /**
     * Cria um novo contrato (fluxo antigo - para vendedores existentes)
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
     * Ativa usuário como vendedor criando um contrato
     * História 2: Processo de Contratação de Vendedores
     */
    @Transactional
    public ContratoDto ativarVendedor(AtivarVendedorRequest request, String adminId) {
        log.info("Ativando usuário {} como vendedor", request.getUsuarioId());

        // Validar se o usuário existe e está ativo
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        if (usuario.getStatus() != UserStatus.ACTIVE) {
            throw new BusinessException("Usuário deve estar ativo para ser transformado em vendedor");
        }

        // Verificar se já é vendedor com contrato ativo
        Vendedor vendedorExistente = vendedorRepository.findByUsuarioId(request.getUsuarioId()).orElse(null);
        if (vendedorExistente != null && vendedorTemContratoAtivo(vendedorExistente.getId())) {
            throw new BusinessException("Usuário já é um vendedor ativo com contrato vigente");
        }

        // Criar vendedor se não existir
        String vendedorId;
        if (vendedorExistente == null) {
            Vendedor novoVendedor = criarVendedor(usuario);
            vendedorId = novoVendedor.getId();
            log.info("Vendedor criado para usuário {}: {}", usuario.getId(), vendedorId);
        } else {
            vendedorId = vendedorExistente.getId();
            log.info("Usando vendedor existente: {}", vendedorId);
        }

        // Criar contrato
        ContratoCreateRequest contratoRequest = new ContratoCreateRequest();
        contratoRequest.setSellerId(vendedorId);
        contratoRequest.setFeeRate(request.getFeeRate());
        contratoRequest.setTerms(request.getTerms());
        contratoRequest.setValidFrom(request.getValidFrom() != null ? request.getValidFrom() : LocalDateTime.now());
        contratoRequest.setValidTo(request.getValidTo());
        contratoRequest.setCategoria(request.getCategoria());

        ContratoDto contrato = criarContrato(contratoRequest, adminId);

        // Ativar imediatamente se solicitado
        if (request.getAtivarImediatamente()) {
            contrato = ativarContrato(contrato.getId(), adminId);
            log.info("Contrato ativado imediatamente para vendedor: {}", vendedorId);
        }

        log.info("Usuário {} transformado em vendedor com sucesso. Contrato: {}", 
                request.getUsuarioId(), contrato.getId());

        return contrato;
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

    /**
     * Cria um novo vendedor para o usuário
     * História 2: Processo de Contratação de Vendedores
     */
    private Vendedor criarVendedor(Usuario usuario) {
        Vendedor vendedor = new Vendedor();
        vendedor.setUsuarioId(usuario.getId());
        
        // Usar dados do usuário como padrão
        vendedor.setCompanyName(usuario.getNome() + " - Vendedor");
        vendedor.setContactEmail(usuario.getEmail());
        vendedor.setContactPhone(usuario.getTelefone());
        
        // Dados padrão para novos vendedores
        vendedor.setDescription("Vendedor ativado via contrato administrativo");
        vendedor.setActive(true);

        return vendedorRepository.save(vendedor);
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

    private void validarCriacaoContratoFromUser(ContratoCreateFromUserRequest request) {
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