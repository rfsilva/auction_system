package com.leilao.modules.produto.controller;

import com.leilao.modules.produto.dto.CatalogoFiltroRequest;
import com.leilao.modules.produto.dto.ProdutoDto;
import com.leilao.modules.produto.service.ProdutoService;
import com.leilao.shared.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Controller público para catálogo de produtos
 */
@RestController
@RequestMapping("/catalogo")
@RequiredArgsConstructor
public class CatalogoController {

    private final ProdutoService produtoService;

    /**
     * Busca produtos no catálogo público com filtros
     */
    @GetMapping("/produtos")
    public ResponseEntity<ApiResponse<Page<ProdutoDto>>> buscarProdutos(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) BigDecimal precoMin,
            @RequestParam(required = false) BigDecimal precoMax,
            @RequestParam(required = false) String titulo,
            @RequestParam(defaultValue = "recentes") String ordenacao,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        
        CatalogoFiltroRequest filtros = new CatalogoFiltroRequest();
        filtros.setCategoria(categoria);
        filtros.setPrecoMin(precoMin);
        filtros.setPrecoMax(precoMax);
        filtros.setTitulo(titulo);
        filtros.setOrdenacao(ordenacao);
        filtros.setPage(page);
        filtros.setSize(size);
        
        Page<ProdutoDto> produtos = produtoService.buscarCatalogoPublico(filtros);
        return ResponseEntity.ok(ApiResponse.success("Produtos encontrados", produtos));
    }

    /**
     * Busca produto específico por ID (público)
     */
    @GetMapping("/produtos/{produtoId}")
    public ResponseEntity<ApiResponse<ProdutoDto>> buscarProduto(
            @PathVariable String produtoId) {
        
        ProdutoDto produto = produtoService.buscarPorId(produtoId);
        
        // Verificar se o produto está ativo para exibição pública
        if (!produto.getIsActive()) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(ApiResponse.success("Produto encontrado", produto));
    }

    /**
     * Lista categorias disponíveis
     */
    @GetMapping("/categorias")
    public ResponseEntity<ApiResponse<List<String>>> listarCategorias() {
        List<String> categorias = produtoService.listarCategoriasAtivas();
        return ResponseEntity.ok(ApiResponse.success("Categorias disponíveis", categorias));
    }
}