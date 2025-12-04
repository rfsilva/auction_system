package com.leilao.modules.produto.controller;

import com.leilao.modules.produto.dto.CatalogoFiltroRequest;
import com.leilao.modules.produto.dto.ProdutoDto;
import com.leilao.modules.produto.service.ProdutoService;
import com.leilao.shared.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

/**
 * Controller público para catálogo de produtos
 * 
 * ⚠️ DEPRECIADO: Este controller está sendo descontinuado.
 * Use o novo sistema de catálogo de lotes: /api/lotes/catalogo-publico
 * 
 * Histórico:
 * - Sprint S2.3: Migração para sistema baseado em lotes
 * - Endpoints de produtos serão removidos em versão futura
 */
@Slf4j
@RestController
@RequestMapping("/catalogo")
@RequiredArgsConstructor
@Deprecated
public class CatalogoController {

    private final ProdutoService produtoService;

    /**
     * ⚠️ DEPRECIADO: Busca produtos no catálogo público com filtros
     * 
     * @deprecated Use o novo endpoint de lotes: GET /api/lotes/catalogo-publico
     * Este endpoint será removido em versão futura.
     * 
     * Migração sugerida:
     * - Antigo: GET /api/catalogo/produtos
     * - Novo: GET /api/lotes/catalogo-publico
     */
    @Deprecated
    @GetMapping("/produtos")
    public ResponseEntity<?> buscarProdutos(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) BigDecimal precoMin,
            @RequestParam(required = false) BigDecimal precoMax,
            @RequestParam(required = false) String titulo,
            @RequestParam(defaultValue = "recentes") String ordenacao,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        
        log.warn("⚠️ ENDPOINT DEPRECIADO: /catalogo/produtos foi acessado. " +
                "Cliente deve migrar para /lotes/catalogo-publico");
        
        // Retornar redirect permanente para o novo endpoint
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
                .location(URI.create("/api/lotes/catalogo-publico"))
                .body(ApiResponse.error(
                    "Endpoint depreciado. Use /api/lotes/catalogo-publico",
                    "DEPRECATED_ENDPOINT"
                ));
    }

    /**
     * ⚠️ DEPRECIADO: Busca produto específico por ID (público)
     * 
     * @deprecated Produtos agora são acessados através de lotes: GET /api/lotes/{loteId}
     * Este endpoint será removido em versão futura.
     * 
     * Migração sugerida:
     * - Antigo: GET /api/catalogo/produtos/{produtoId}
     * - Novo: GET /api/lotes/{loteId} (e navegar pelos produtos do lote)
     */
    @Deprecated
    @GetMapping("/produtos/{produtoId}")
    public ResponseEntity<?> buscarProduto(@PathVariable String produtoId) {
        
        log.warn("⚠️ ENDPOINT DEPRECIADO: /catalogo/produtos/{} foi acessado. " +
                "Cliente deve migrar para navegação por lotes", produtoId);
        
        // Tentar encontrar o lote que contém este produto
        try {
            ProdutoDto produto = produtoService.buscarPorId(produtoId);
            
            if (produto.getLoteId() != null) {
                // Redirect para o lote que contém o produto
                String loteUrl = "/api/lotes/" + produto.getLoteId();
                return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
                        .location(URI.create(loteUrl))
                        .body(ApiResponse.error(
                            "Produto agora é acessado através do lote: " + loteUrl,
                            "DEPRECATED_ENDPOINT"
                        ));
            } else {
                // Produto sem lote - não é mais público
                return ResponseEntity.status(HttpStatus.GONE)
                        .body(ApiResponse.error(
                            "Produto não está mais disponível publicamente. Apenas produtos em lotes são exibidos.",
                            "PRODUCT_NOT_PUBLIC"
                        ));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(
                        "Produto não encontrado. Use o catálogo de lotes: /api/lotes/catalogo-publico",
                        "PRODUCT_NOT_FOUND"
                    ));
        }
    }

    /**
     * ✅ MANTIDO: Lista categorias disponíveis
     * 
     * Este endpoint continua ativo pois é usado tanto por lotes quanto produtos.
     */
    @GetMapping("/categorias")
    public ResponseEntity<ApiResponse<List<String>>> listarCategorias() {
        List<String> categorias = produtoService.listarCategoriasAtivas();
        return ResponseEntity.ok(ApiResponse.success("Categorias disponíveis", categorias));
    }
}