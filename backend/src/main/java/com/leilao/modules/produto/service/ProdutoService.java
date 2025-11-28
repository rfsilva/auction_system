package com.leilao.modules.produto.service;

import com.leilao.modules.produto.dto.*;
import com.leilao.modules.produto.entity.Produto;
import com.leilao.modules.produto.repository.ProdutoRepository;
import com.leilao.modules.vendedor.service.VendedorService;
import com.leilao.shared.enums.ProdutoStatus;
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

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service para operações com Produto
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final VendedorService vendedorService;

    /**
     * Cria um novo produto
     */
    @Transactional
    public ProdutoDto criarProduto(ProdutoCreateRequest request, String usuarioId) {
        log.info("Criando produto para usuário: {}", usuarioId);
        
        // Buscar o ID do vendedor pelo ID do usuário
        String vendedorId = vendedorService.obterVendedorIdPorUsuarioId(usuarioId);
        log.info("Vendedor ID encontrado: {} para usuário: {}", vendedorId, usuarioId);
        
        // Validações de negócio
        validarCriacaoProduto(request);
        
        Produto produto = new Produto();
        produto.setSellerId(vendedorId); // Usar o ID do vendedor, não do usuário
        produto.setTitle(request.getTitle());
        produto.setDescription(request.getDescription());
        produto.setImagesList(request.getImages());
        produto.setWeight(request.getWeight());
        produto.setDimensions(request.getDimensions());
        produto.setInitialPrice(request.getInitialPrice());
        produto.setCurrentPrice(request.getInitialPrice());
        produto.setReservePrice(request.getReservePrice());
        produto.setIncrementMin(request.getIncrementMin());
        produto.setEndDateTime(request.getEndDateTime());
        produto.setCategoria(request.getCategoria());
        produto.setTagsList(request.getTags());
        produto.setAntiSnipeEnabled(request.getAntiSnipeEnabled());
        produto.setAntiSnipeExtension(request.getAntiSnipeExtension());
        produto.setStatus(ProdutoStatus.DRAFT);
        
        produto = produtoRepository.save(produto);
        
        log.info("Produto criado com sucesso: {} para vendedor: {}", produto.getId(), vendedorId);
        return convertToDto(produto);
    }

    /**
     * Atualiza um produto existente
     */
    @Transactional
    public ProdutoDto atualizarProduto(String produtoId, ProdutoUpdateRequest request, String usuarioId) {
        log.info("Atualizando produto: {} por usuário: {}", produtoId, usuarioId);
        
        // Buscar o ID do vendedor pelo ID do usuário
        String vendedorId = vendedorService.obterVendedorIdPorUsuarioId(usuarioId);
        
        Produto produto = buscarProdutoPorId(produtoId);
        
        // Verificar se o vendedor é o dono do produto
        if (!produto.getSellerId().equals(vendedorId)) {
            throw new BusinessException("Você não tem permissão para editar este produto");
        }
        
        // Validar se o produto pode ser editado
        validarEdicaoProduto(produto);
        
        // Atualizar campos não nulos
        if (request.getTitle() != null) {
            produto.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            produto.setDescription(request.getDescription());
        }
        if (request.getImages() != null) {
            produto.setImagesList(request.getImages());
        }
        if (request.getWeight() != null) {
            produto.setWeight(request.getWeight());
        }
        if (request.getDimensions() != null) {
            produto.setDimensions(request.getDimensions());
        }
        if (request.getInitialPrice() != null) {
            produto.setInitialPrice(request.getInitialPrice());
            // Se não há lances ainda, atualizar preço atual também
            if (produto.getCurrentPrice().equals(produto.getInitialPrice())) {
                produto.setCurrentPrice(request.getInitialPrice());
            }
        }
        if (request.getReservePrice() != null) {
            produto.setReservePrice(request.getReservePrice());
        }
        if (request.getIncrementMin() != null) {
            produto.setIncrementMin(request.getIncrementMin());
        }
        if (request.getEndDateTime() != null) {
            produto.setEndDateTime(request.getEndDateTime());
        }
        if (request.getCategoria() != null) {
            produto.setCategoria(request.getCategoria());
        }
        if (request.getTags() != null) {
            produto.setTagsList(request.getTags());
        }
        if (request.getAntiSnipeEnabled() != null) {
            produto.setAntiSnipeEnabled(request.getAntiSnipeEnabled());
        }
        if (request.getAntiSnipeExtension() != null) {
            produto.setAntiSnipeExtension(request.getAntiSnipeExtension());
        }
        
        produto = produtoRepository.save(produto);
        
        log.info("Produto atualizado com sucesso: {}", produto.getId());
        return convertToDto(produto);
    }

    /**
     * Busca produto por ID
     */
    @Transactional(readOnly = true)
    public ProdutoDto buscarPorId(String produtoId) {
        Produto produto = buscarProdutoPorId(produtoId);
        return convertToDto(produto);
    }

    /**
     * Lista produtos do vendedor
     */
    @Transactional(readOnly = true)
    public Page<ProdutoDto> listarProdutosVendedor(String usuarioId, Pageable pageable) {
        // Buscar o ID do vendedor pelo ID do usuário
        String vendedorId = vendedorService.obterVendedorIdPorUsuarioId(usuarioId);
        
        Page<Produto> produtos = produtoRepository.findBySellerId(vendedorId, pageable);
        return produtos.map(this::convertToDto);
    }

    /**
     * Exclui um produto
     */
    @Transactional
    public void excluirProduto(String produtoId, String usuarioId) {
        log.info("Excluindo produto: {} por usuário: {}", produtoId, usuarioId);
        
        // Buscar o ID do vendedor pelo ID do usuário
        String vendedorId = vendedorService.obterVendedorIdPorUsuarioId(usuarioId);
        
        Produto produto = buscarProdutoPorId(produtoId);
        
        // Verificar se o vendedor é o dono do produto
        if (!produto.getSellerId().equals(vendedorId)) {
            throw new BusinessException("Você não tem permissão para excluir este produto");
        }
        
        // Validar se o produto pode ser excluído
        validarExclusaoProduto(produto);
        
        produtoRepository.delete(produto);
        
        log.info("Produto excluído com sucesso: {}", produtoId);
    }

    /**
     * Publica um produto (muda status para ACTIVE)
     */
    @Transactional
    public ProdutoDto publicarProduto(String produtoId, String usuarioId) {
        log.info("Publicando produto: {} por usuário: {}", produtoId, usuarioId);
        
        // Buscar o ID do vendedor pelo ID do usuário
        String vendedorId = vendedorService.obterVendedorIdPorUsuarioId(usuarioId);
        
        Produto produto = buscarProdutoPorId(produtoId);
        
        // Verificar se o vendedor é o dono do produto
        if (!produto.getSellerId().equals(vendedorId)) {
            throw new BusinessException("Você não tem permissão para publicar este produto");
        }
        
        // Validar se o produto pode ser publicado
        validarPublicacaoProduto(produto);
        
        produto.setStatus(ProdutoStatus.ACTIVE);
        produto = produtoRepository.save(produto);
        
        log.info("Produto publicado com sucesso: {}", produtoId);
        return convertToDto(produto);
    }

    /**
     * Busca catálogo público com filtros
     */
    @Transactional(readOnly = true)
    public Page<ProdutoDto> buscarCatalogoPublico(CatalogoFiltroRequest filtros) {
        Pageable pageable = criarPageable(filtros);
        
        Page<Produto> produtos = produtoRepository.findCatalogoPublico(
            LocalDateTime.now(),
            filtros.getCategoria(),
            filtros.getPrecoMin(),
            filtros.getPrecoMax(),
            filtros.getTitulo(),
            pageable
        );
        
        return produtos.map(this::convertToDto);
    }

    /**
     * Lista categorias ativas
     */
    @Transactional(readOnly = true)
    public List<String> listarCategoriasAtivas() {
        return produtoRepository.findCategoriasAtivas();
    }

    /**
     * Atualiza produtos expirados
     */
    @Transactional
    public void atualizarProdutosExpirados() {
        List<Produto> produtosExpirados = produtoRepository.findProdutosExpirados(LocalDateTime.now());
        
        for (Produto produto : produtosExpirados) {
            produto.setStatus(ProdutoStatus.EXPIRED);
            produtoRepository.save(produto);
        }
        
        if (!produtosExpirados.isEmpty()) {
            log.info("Atualizados {} produtos expirados", produtosExpirados.size());
        }
    }

    // Métodos privados

    private Produto buscarProdutoPorId(String produtoId) {
        return produtoRepository.findById(produtoId)
            .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado: " + produtoId));
    }

    private void validarCriacaoProduto(ProdutoCreateRequest request) {
        // Validar data de encerramento
        if (request.getEndDateTime().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new BusinessException("Data de encerramento deve ser pelo menos 1 hora no futuro");
        }
        
        // Validar preço de reserva
        if (request.getReservePrice() != null && 
            request.getReservePrice().compareTo(request.getInitialPrice()) < 0) {
            throw new BusinessException("Preço de reserva não pode ser menor que o preço inicial");
        }
    }

    private void validarEdicaoProduto(Produto produto) {
        if (produto.isActive() && produto.getCurrentPrice().compareTo(produto.getInitialPrice()) > 0) {
            throw new BusinessException("Produto com lances não pode ser editado");
        }
        
        if (produto.isSold()) {
            throw new BusinessException("Produto vendido não pode ser editado");
        }
    }

    private void validarExclusaoProduto(Produto produto) {
        if (produto.isActive() && produto.getCurrentPrice().compareTo(produto.getInitialPrice()) > 0) {
            throw new BusinessException("Produto com lances não pode ser excluído");
        }
        
        if (produto.isSold()) {
            throw new BusinessException("Produto vendido não pode ser excluído");
        }
    }

    private void validarPublicacaoProduto(Produto produto) {
        if (!produto.isDraft()) {
            throw new BusinessException("Apenas produtos em rascunho podem ser publicados");
        }
        
        if (produto.getEndDateTime().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new BusinessException("Data de encerramento deve ser pelo menos 1 hora no futuro");
        }
        
        // TODO: Validar se vendedor está ativo e verificado
        // TODO: Validar se produto tem pelo menos uma imagem
    }

    private Pageable criarPageable(CatalogoFiltroRequest filtros) {
        Sort sort = switch (filtros.getOrdenacao()) {
            case "preco_asc" -> Sort.by("currentPrice").ascending();
            case "preco_desc" -> Sort.by("currentPrice").descending();
            case "terminando" -> Sort.by("endDateTime").ascending();
            default -> Sort.by("createdAt").descending();
        };
        
        return PageRequest.of(filtros.getPage(), filtros.getSize(), sort);
    }

    private ProdutoDto convertToDto(Produto produto) {
        ProdutoDto dto = new ProdutoDto();
        dto.setId(produto.getId());
        dto.setSellerId(produto.getSellerId());
        dto.setLoteId(produto.getLoteId());
        dto.setTitle(produto.getTitle());
        dto.setDescription(produto.getDescription());
        dto.setImages(produto.getImagesList());
        dto.setWeight(produto.getWeight());
        dto.setDimensions(produto.getDimensions());
        dto.setInitialPrice(produto.getInitialPrice());
        dto.setCurrentPrice(produto.getCurrentPrice());
        dto.setReservePrice(produto.getReservePrice());
        dto.setIncrementMin(produto.getIncrementMin());
        dto.setEndDateTime(produto.getEndDateTime());
        dto.setStatus(produto.getStatus());
        dto.setModerated(produto.getModerated());
        dto.setModeratorId(produto.getModeratorId());
        dto.setModeratedAt(produto.getModeratedAt());
        dto.setAntiSnipeEnabled(produto.getAntiSnipeEnabled());
        dto.setAntiSnipeExtension(produto.getAntiSnipeExtension());
        dto.setCategoria(produto.getCategoria());
        dto.setTags(produto.getTagsList());
        dto.setCreatedAt(produto.getCreatedAt());
        dto.setUpdatedAt(produto.getUpdatedAt());
        
        // Campos calculados
        dto.setIsActive(produto.isActive());
        dto.setIsExpired(produto.isExpired());
        dto.setCanReceiveBids(produto.canReceiveBids());
        
        if (produto.getEndDateTime() != null) {
            Duration duration = Duration.between(LocalDateTime.now(), produto.getEndDateTime());
            dto.setTimeRemaining(Math.max(0, duration.getSeconds()));
        }
        
        return dto;
    }
}