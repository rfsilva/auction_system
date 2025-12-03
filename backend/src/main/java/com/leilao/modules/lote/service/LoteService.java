package com.leilao.modules.lote.service;

import com.leilao.modules.contrato.entity.Contrato;
import com.leilao.modules.contrato.repository.ContratoRepository;
import com.leilao.modules.lote.dto.LoteCreateRequest;
import com.leilao.modules.lote.dto.LoteDto;
import com.leilao.modules.lote.dto.LoteUpdateRequest;
import com.leilao.modules.lote.entity.Lote;
import com.leilao.modules.lote.repository.LoteRepository;
import com.leilao.modules.produto.entity.Produto;
import com.leilao.modules.produto.repository.ProdutoRepository;
import com.leilao.modules.vendedor.entity.Vendedor;
import com.leilao.modules.vendedor.repository.VendedorRepository;
import com.leilao.modules.vendedor.service.VendedorService;
import com.leilao.modules.auth.entity.Usuario;
import com.leilao.modules.auth.repository.UsuarioRepository;
import com.leilao.shared.enums.LoteStatus;
import com.leilao.shared.enums.ProdutoStatus;
import com.leilao.shared.exception.BusinessException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service para operações de Lote com suporte a i18n usando MessageSourceAccessor
 * Atualizado para usar contratos ao invés de vendedores diretamente
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LoteService {

    private final LoteRepository loteRepository;
    private final ProdutoRepository produtoRepository;
    private final VendedorService vendedorService;
    private final ContratoRepository contratoRepository;
    private final VendedorRepository vendedorRepository;
    private final UsuarioRepository usuarioRepository;
    private final MessageSourceAccessor messageSourceAccessor;

    /**
     * Cria um novo lote
     */
    @Transactional
    public LoteDto criarLote(LoteCreateRequest request, String usuarioId) {
        log.info("Criando novo lote para usuário: {}", usuarioId);
        
        // Obter ID do vendedor a partir do ID do usuário
        String vendedorId = vendedorService.obterVendedorIdPorUsuarioId(usuarioId);
        
        // Buscar contrato ativo do vendedor
        Contrato contratoAtivo;
        if (request.getContractId() != null && !request.getContractId().trim().isEmpty()) {
            // Usar contrato específico escolhido pelo vendedor
            contratoAtivo = buscarContratoPorId(request.getContractId());
            
            // Verificar se o contrato pertence ao vendedor
            if (!contratoAtivo.getSellerId().equals(vendedorId)) {
                throw new BusinessException(
                        messageSourceAccessor.getMessage("auth.access.denied", LocaleContextHolder.getLocale()));
            }
            
            // Verificar se o contrato está ativo
            if (!contratoAtivo.isActive()) {
                throw new BusinessException(
                        messageSourceAccessor.getMessage("contract.cannot.activate", LocaleContextHolder.getLocale()));
            }
            
            log.info("Usando contrato específico escolhido pelo vendedor: {}", contratoAtivo.getId());
        } else {
            // Buscar contrato ativo automaticamente por categoria
            contratoAtivo = buscarContratoAtivoDoVendedor(vendedorId, request.getCategoria());
            log.info("Contrato encontrado automaticamente por categoria: {}", contratoAtivo.getId());
        }
        
        validarCriacaoLote(request);
        
        Lote lote = Lote.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .loteEndDateTime(request.getLoteEndDateTime())
                .contractId(contratoAtivo.getId())
                .sellerId(vendedorId) // Manter temporariamente para compatibilidade
                .status(LoteStatus.DRAFT)
                .build();
        
        lote = loteRepository.save(lote);
        
        // Associar produtos ao lote se fornecidos
        if (request.getProdutoIds() != null && !request.getProdutoIds().isEmpty()) {
            associarProdutosAoLote(lote.getId(), request.getProdutoIds(), usuarioId);
        }
        
        log.info("Lote criado com sucesso: {} para contrato: {}", lote.getId(), contratoAtivo.getId());
        return convertToDto(lote);
    }

    /**
     * Atualiza um lote existente
     */
    @Transactional
    public LoteDto atualizarLote(String loteId, LoteUpdateRequest request, String usuarioId) {
        log.info("Atualizando lote: {} para usuário: {}", loteId, usuarioId);
        
        // Obter ID do vendedor a partir do ID do usuário
        String vendedorId = vendedorService.obterVendedorIdPorUsuarioId(usuarioId);
        
        Lote lote = buscarLotePorId(loteId);
        
        // Verificar se o vendedor é o proprietário através do contrato
        Contrato contrato = buscarContratoPorId(lote.getContractId());
        if (!contrato.getSellerId().equals(vendedorId)) {
            throw new BusinessException(
                    messageSourceAccessor.getMessage("auth.access.denied", LocaleContextHolder.getLocale()));
        }
        
        // Verificar se o contrato ainda está ativo
        if (!contrato.isActive()) {
            throw new BusinessException(
                    messageSourceAccessor.getMessage("contract.cannot.edit", LocaleContextHolder.getLocale()));
        }
        
        validarEdicaoLote(lote);
        
        // Atualizar campos se fornecidos
        if (request.getTitle() != null) {
            lote.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            lote.setDescription(request.getDescription());
        }
        if (request.getLoteEndDateTime() != null) {
            validarDataEncerramento(request.getLoteEndDateTime());
            lote.setLoteEndDateTime(request.getLoteEndDateTime());
        }
        
        lote = loteRepository.save(lote);
        
        // Atualizar associação de produtos se fornecida
        if (request.getProdutoIds() != null) {
            atualizarAssociacaoProdutos(lote.getId(), request.getProdutoIds(), usuarioId);
        }
        
        log.info("Lote atualizado com sucesso: {}", loteId);
        return convertToDto(lote);
    }

    /**
     * Busca lote por ID
     */
    @Transactional(readOnly = true)
    public LoteDto buscarPorId(String loteId) {
        Lote lote = buscarLotePorId(loteId);
        return convertToDto(lote);
    }

    /**
     * Lista lotes do vendedor
     */
    @Transactional(readOnly = true)
    public Page<LoteDto> listarLotesVendedor(String usuarioId, Pageable pageable) {
        // Obter ID do vendedor a partir do ID do usuário
        String vendedorId = vendedorService.obterVendedorIdPorUsuarioId(usuarioId);
        
        Page<Lote> lotes = loteRepository.findBySellerId(vendedorId, pageable);
        return lotes.map(this::convertToDto);
    }

    /**
     * Lista lotes públicos (para visitantes)
     */
    @Transactional(readOnly = true)
    public Page<LoteDto> listarLotesPublicos(String termo, Pageable pageable) {
        Page<Lote> lotes;
        if (termo != null && !termo.trim().isEmpty()) {
            lotes = loteRepository.findLotesPublicosComFiltros(termo.trim(), pageable);
        } else {
            lotes = loteRepository.findLotesPublicos(pageable);
        }
        return lotes.map(this::convertToDto);
    }

    /**
     * Ativa um lote
     */
    @Transactional
    public LoteDto ativarLote(String loteId, String usuarioId) {
        log.info("Ativando lote: {} para usuário: {}", loteId, usuarioId);
        
        // Obter ID do vendedor a partir do ID do usuário
        String vendedorId = vendedorService.obterVendedorIdPorUsuarioId(usuarioId);
        
        Lote lote = buscarLotePorId(loteId);
        
        // Verificar se o vendedor é o proprietário através do contrato
        Contrato contrato = buscarContratoPorId(lote.getContractId());
        if (!contrato.getSellerId().equals(vendedorId)) {
            throw new BusinessException(
                    messageSourceAccessor.getMessage("auth.access.denied", LocaleContextHolder.getLocale()));
        }
        
        // Verificar se o contrato ainda está ativo
        if (!contrato.isActive()) {
            throw new BusinessException(
                    messageSourceAccessor.getMessage("contract.cannot.activate", LocaleContextHolder.getLocale()));
        }
        
        validarAtivacaoLote(lote);
        
        lote.activate();
        lote = loteRepository.save(lote);
        
        // Ativar produtos associados
        ativarProdutosDoLote(loteId);
        
        log.info("Lote ativado com sucesso: {}", loteId);
        return convertToDto(lote);
    }

    /**
     * Cancela um lote
     */
    @Transactional
    public LoteDto cancelarLote(String loteId, String usuarioId) {
        log.info("Cancelando lote: {} para usuário: {}", loteId, usuarioId);
        
        // Obter ID do vendedor a partir do ID do usuário
        String vendedorId = vendedorService.obterVendedorIdPorUsuarioId(usuarioId);
        
        Lote lote = buscarLotePorId(loteId);
        
        // Verificar se o vendedor é o proprietário através do contrato
        Contrato contrato = buscarContratoPorId(lote.getContractId());
        if (!contrato.getSellerId().equals(vendedorId)) {
            throw new BusinessException(
                    messageSourceAccessor.getMessage("auth.access.denied", LocaleContextHolder.getLocale()));
        }
        
        lote.cancel();
        lote = loteRepository.save(lote);
        
        // Cancelar produtos associados
        cancelarProdutosDoLote(loteId);
        
        log.info("Lote cancelado com sucesso: {}", loteId);
        return convertToDto(lote);
    }

    /**
     * Exclui um lote
     */
    @Transactional
    public void excluirLote(String loteId, String usuarioId) {
        log.info("Excluindo lote: {} para usuário: {}", loteId, usuarioId);
        
        // Obter ID do vendedor a partir do ID do usuário
        String vendedorId = vendedorService.obterVendedorIdPorUsuarioId(usuarioId);
        
        Lote lote = buscarLotePorId(loteId);
        
        // Verificar se o vendedor é o proprietário através do contrato
        Contrato contrato = buscarContratoPorId(lote.getContractId());
        if (!contrato.getSellerId().equals(vendedorId)) {
            throw new BusinessException(
                    messageSourceAccessor.getMessage("auth.access.denied", LocaleContextHolder.getLocale()));
        }
        
        validarExclusaoLote(lote);
        
        // Desassociar produtos do lote
        desassociarProdutosDoLote(loteId);
        
        loteRepository.delete(lote);
        
        log.info("Lote excluído com sucesso: {}", loteId);
    }

    /**
     * Atualiza lotes expirados
     */
    @Transactional
    public void atualizarLotesExpirados() {
        List<Lote> lotesExpirados = loteRepository.findLotesExpirados(LocalDateTime.now());
        
        for (Lote lote : lotesExpirados) {
            lote.close();
            loteRepository.save(lote);
            log.info("Lote {} marcado como fechado por expiração", lote.getId());
        }
    }

    // Métodos auxiliares

    private Lote buscarLotePorId(String loteId) {
        return loteRepository.findById(loteId)
                .orElseThrow(() -> new EntityNotFoundException(
                        messageSourceAccessor.getMessage("lot.not.found", LocaleContextHolder.getLocale())));
    }

    private Contrato buscarContratoPorId(String contratoId) {
        return contratoRepository.findById(contratoId)
                .orElseThrow(() -> new EntityNotFoundException(
                        messageSourceAccessor.getMessage("contract.not.found", LocaleContextHolder.getLocale())));
    }

    private Contrato buscarContratoAtivoDoVendedor(String vendedorId, String categoria) {
        return contratoRepository.findContratoAtivoParaCategoria(vendedorId, categoria, LocalDateTime.now())
                .orElseThrow(() -> new BusinessException(
                        messageSourceAccessor.getMessage("seller.contract.required", LocaleContextHolder.getLocale())));
    }

    private void validarCriacaoLote(LoteCreateRequest request) {
        // Validar título
        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new BusinessException(
                    messageSourceAccessor.getMessage("lot.title.required", LocaleContextHolder.getLocale()));
        }
        
        // Validar descrição
        if (request.getDescription() == null || request.getDescription().trim().isEmpty()) {
            throw new BusinessException(
                    messageSourceAccessor.getMessage("lot.description.required", LocaleContextHolder.getLocale()));
        }
        
        validarDataEncerramento(request.getLoteEndDateTime());
    }

    private void validarDataEncerramento(LocalDateTime dataEncerramento) {
        if (dataEncerramento == null || dataEncerramento.isBefore(LocalDateTime.now().plusHours(1))) {
            throw new BusinessException(
                    messageSourceAccessor.getMessage("lot.date.invalid", LocaleContextHolder.getLocale()));
        }
    }

    private void validarEdicaoLote(Lote lote) {
        if (!lote.canBeEdited()) {
            throw new BusinessException(
                    messageSourceAccessor.getMessage("lot.cannot.edit", LocaleContextHolder.getLocale()));
        }
    }

    private void validarAtivacaoLote(Lote lote) {
        if (!lote.canBeActivated()) {
            throw new BusinessException(
                    messageSourceAccessor.getMessage("lot.cannot.activate", LocaleContextHolder.getLocale()));
        }
        
        // Verificar se há produtos associados
        List<Produto> produtos = produtoRepository.findByLoteId(lote.getId());
        if (produtos.isEmpty()) {
            throw new BusinessException(
                    messageSourceAccessor.getMessage("lot.products.required", LocaleContextHolder.getLocale()));
        }
    }

    private void validarExclusaoLote(Lote lote) {
        if (!lote.isDraft()) {
            throw new BusinessException(
                    messageSourceAccessor.getMessage("lot.cannot.delete", LocaleContextHolder.getLocale()));
        }
    }

    private void associarProdutosAoLote(String loteId, List<String> produtoIds, String usuarioId) {
        // Obter ID do vendedor a partir do ID do usuário
        String vendedorId = vendedorService.obterVendedorIdPorUsuarioId(usuarioId);
        
        for (String produtoId : produtoIds) {
            Produto produto = produtoRepository.findById(produtoId)
                    .orElseThrow(() -> new EntityNotFoundException(
                            messageSourceAccessor.getMessage("product.not.found", LocaleContextHolder.getLocale())));
            
            // Verificar se o produto pertence ao vendedor
            if (!produto.getSellerId().equals(vendedorId)) {
                throw new BusinessException(
                        messageSourceAccessor.getMessage("auth.access.denied", LocaleContextHolder.getLocale()));
            }
            
            // Verificar se o produto pode ser associado a um lote
            if (!produto.isDraft()) {
                throw new BusinessException(
                        messageSourceAccessor.getMessage("product.cannot.edit", LocaleContextHolder.getLocale()));
            }
            
            produto.setLoteId(loteId);
            produtoRepository.save(produto);
        }
    }

    private void atualizarAssociacaoProdutos(String loteId, List<String> novosProdutoIds, String usuarioId) {
        // Desassociar produtos atuais
        List<Produto> produtosAtuais = produtoRepository.findByLoteId(loteId);
        for (Produto produto : produtosAtuais) {
            produto.setLoteId(null);
            produtoRepository.save(produto);
        }
        
        // Associar novos produtos
        if (!novosProdutoIds.isEmpty()) {
            associarProdutosAoLote(loteId, novosProdutoIds, usuarioId);
        }
    }

    private void ativarProdutosDoLote(String loteId) {
        List<Produto> produtos = produtoRepository.findByLoteId(loteId);
        for (Produto produto : produtos) {
            if (produto.isDraft()) {
                produto.setStatus(ProdutoStatus.ACTIVE);
                produtoRepository.save(produto);
            }
        }
    }

    private void cancelarProdutosDoLote(String loteId) {
        List<Produto> produtos = produtoRepository.findByLoteId(loteId);
        for (Produto produto : produtos) {
            if (produto.isActive() || produto.isDraft()) {
                produto.setStatus(ProdutoStatus.CANCELLED);
                produtoRepository.save(produto);
            }
        }
    }

    private void desassociarProdutosDoLote(String loteId) {
        List<Produto> produtos = produtoRepository.findByLoteId(loteId);
        for (Produto produto : produtos) {
            produto.setLoteId(null);
            produtoRepository.save(produto);
        }
    }

    private LoteDto convertToDto(Lote lote) {
        // Buscar produtos associados
        List<Produto> produtos = produtoRepository.findByLoteId(lote.getId());
        List<String> produtoIds = produtos.stream()
                .map(Produto::getId)
                .collect(Collectors.toList());
        
        // Calcular tempo restante
        long timeRemaining = 0;
        if (lote.getLoteEndDateTime() != null) {
            Duration duration = Duration.between(LocalDateTime.now(), lote.getLoteEndDateTime());
            timeRemaining = Math.max(0, duration.getSeconds());
        }
        
        // Buscar informações do contrato e vendedor
        String sellerId = null;
        String sellerName = null;
        String sellerCompanyName = null;
        String contractStatus = null;
        String categoria = null;
        
        try {
            Contrato contrato = buscarContratoPorId(lote.getContractId());
            sellerId = contrato.getSellerId();
            contractStatus = messageSourceAccessor.getMessage(contrato.getStatus().getDisplayName(), LocaleContextHolder.getLocale());
            categoria = contrato.getCategoria();
            
            // Buscar informações do vendedor
            Vendedor vendedor = vendedorRepository.findById(contrato.getSellerId()).orElse(null);
            if (vendedor != null) {
                sellerCompanyName = vendedor.getCompanyName();
                
                // Buscar nome do usuário
                Usuario usuario = usuarioRepository.findById(vendedor.getUsuarioId()).orElse(null);
                if (usuario != null) {
                    sellerName = usuario.getNome();
                }
            }
        } catch (Exception e) {
            log.warn("Erro ao buscar informações do contrato/vendedor para lote: {}", lote.getId(), e);
        }
        
        return LoteDto.builder()
                .id(lote.getId())
                .contractId(lote.getContractId())
                .title(lote.getTitle())
                .description(lote.getDescription())
                .loteEndDateTime(lote.getLoteEndDateTime())
                .status(lote.getStatus())
                .createdAt(lote.getCreatedAt())
                .updatedAt(lote.getUpdatedAt())
                .isActive(lote.isActive())
                .isExpired(lote.isExpired())
                .canBeEdited(lote.canBeEdited())
                .canBeActivated(lote.canBeActivated())
                .canBeCancelled(lote.canBeCancelled())
                .timeRemaining(timeRemaining)
                .produtoIds(produtoIds)
                .totalProdutos(produtos.size())
                // Informações do contrato e vendedor
                .sellerId(sellerId)
                .sellerName(sellerName)
                .sellerCompanyName(sellerCompanyName)
                .contractStatus(contractStatus)
                .categoria(categoria)
                .build();
    }
}