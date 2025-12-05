package com.leilao.modules.produto.controller;

import com.leilao.modules.auth.entity.Usuario;
import com.leilao.modules.produto.dto.*;
import com.leilao.modules.produto.service.ProdutoService;
import com.leilao.shared.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para operações CRUD de produtos (vendedores)
 * FASE 1 - Reorganização de Rotas: Movido para /api/vendedor/produtos/**
 * 
 * Requer autenticação e role SELLER
 */
@RestController
@RequestMapping("/api/vendedor/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private static final Logger logger = LoggerFactory.getLogger(ProdutoController.class);
    private final ProdutoService produtoService;

    /**
     * Cria um novo produto
     */
    @PostMapping
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ApiResponse<ProdutoDto>> criarProduto(
            @Valid @RequestBody ProdutoCreateRequest request,
            @AuthenticationPrincipal Usuario usuario) {
        
        // Debug logs
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        logger.info("=== DEBUG CRIAR PRODUTO ===");
        logger.info("Authentication: {}", auth != null ? auth.getClass().getSimpleName() : "null");
        logger.info("Principal: {}", auth != null ? auth.getPrincipal().getClass().getSimpleName() : "null");
        logger.info("Usuario parameter: {}", usuario != null ? usuario.getClass().getSimpleName() : "null");
        
        if (auth != null && auth.getPrincipal() != null) {
            logger.info("Principal details: {}", auth.getPrincipal().toString());
        }
        
        if (usuario == null) {
            logger.error("Usuario é null! Verificando authentication principal...");
            if (auth != null && auth.getPrincipal() instanceof Usuario) {
                usuario = (Usuario) auth.getPrincipal();
                logger.info("Usuario recuperado do authentication: {}", usuario.getEmail());
            } else {
                logger.error("Principal não é uma instância de Usuario!");
                throw new RuntimeException("Usuário não autenticado corretamente");
            }
        }
        
        ProdutoDto produto = produtoService.criarProduto(request, usuario.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success("Produto criado com sucesso", produto));
    }

    /**
     * Atualiza um produto existente
     */
    @PutMapping("/{produtoId}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ApiResponse<ProdutoDto>> atualizarProduto(
            @PathVariable String produtoId,
            @Valid @RequestBody ProdutoUpdateRequest request,
            @AuthenticationPrincipal Usuario usuario) {
        
        if (usuario == null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() instanceof Usuario) {
                usuario = (Usuario) auth.getPrincipal();
            }
        }
        
        ProdutoDto produto = produtoService.atualizarProduto(produtoId, request, usuario.getId());
        return ResponseEntity.ok(ApiResponse.success("Produto atualizado com sucesso", produto));
    }

    /**
     * Busca produto por ID
     */
    @GetMapping("/{produtoId}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ApiResponse<ProdutoDto>> buscarProduto(
            @PathVariable String produtoId) {
        
        ProdutoDto produto = produtoService.buscarPorId(produtoId);
        return ResponseEntity.ok(ApiResponse.success(produto));
    }

    /**
     * Lista produtos do vendedor logado
     */
    @GetMapping
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ApiResponse<Page<ProdutoDto>>> listarMeusProdutos(
            @AuthenticationPrincipal Usuario usuario,
            @PageableDefault(size = 20) Pageable pageable) {
        
        if (usuario == null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() instanceof Usuario) {
                usuario = (Usuario) auth.getPrincipal();
            }
        }
        
        Page<ProdutoDto> produtos = produtoService.listarProdutosVendedor(usuario.getId(), pageable);
        return ResponseEntity.ok(ApiResponse.success("Produtos listados com sucesso", produtos));
    }

    /**
     * Exclui um produto
     */
    @DeleteMapping("/{produtoId}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ApiResponse<Void>> excluirProduto(
            @PathVariable String produtoId,
            @AuthenticationPrincipal Usuario usuario) {
        
        if (usuario == null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() instanceof Usuario) {
                usuario = (Usuario) auth.getPrincipal();
            }
        }
        
        produtoService.excluirProduto(produtoId, usuario.getId());
        return ResponseEntity.ok(ApiResponse.success("Produto excluído com sucesso", null));
    }

    /**
     * Publica um produto (muda status para ACTIVE)
     */
    @PostMapping("/{produtoId}/publicar")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ApiResponse<ProdutoDto>> publicarProduto(
            @PathVariable String produtoId,
            @AuthenticationPrincipal Usuario usuario) {
        
        if (usuario == null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() instanceof Usuario) {
                usuario = (Usuario) auth.getPrincipal();
            }
        }
        
        ProdutoDto produto = produtoService.publicarProduto(produtoId, usuario.getId());
        return ResponseEntity.ok(ApiResponse.success("Produto publicado com sucesso", produto));
    }

    /**
     * Lista categorias ativas
     */
    @GetMapping("/categorias")
    public ResponseEntity<ApiResponse<List<String>>> listarCategorias() {
        List<String> categorias = produtoService.listarCategoriasAtivas();
        return ResponseEntity.ok(ApiResponse.success("Categorias listadas com sucesso", categorias));
    }
}