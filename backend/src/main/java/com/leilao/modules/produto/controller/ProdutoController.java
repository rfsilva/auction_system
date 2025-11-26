package com.leilao.modules.produto.controller;

import com.leilao.modules.auth.entity.Usuario;
import com.leilao.modules.produto.dto.*;
import com.leilao.modules.produto.service.ProdutoService;
import com.leilao.shared.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para operações CRUD de produtos (vendedores)
 */
@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    /**
     * Cria um novo produto
     */
    @PostMapping
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ApiResponse<ProdutoDto>> criarProduto(
            @Valid @RequestBody ProdutoCreateRequest request,
            @AuthenticationPrincipal Usuario usuario) {
        
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
    @GetMapping("/meus-produtos")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ApiResponse<Page<ProdutoDto>>> listarMeusProdutos(
            @AuthenticationPrincipal Usuario usuario,
            @PageableDefault(size = 20) Pageable pageable) {
        
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