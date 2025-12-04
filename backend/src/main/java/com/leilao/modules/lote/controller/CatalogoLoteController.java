package com.leilao.modules.lote.controller;

import com.leilao.modules.lote.dto.LoteDto;
import com.leilao.modules.lote.service.LoteService;
import com.leilao.modules.produto.service.ProdutoService;
import com.leilao.shared.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller público para catálogo de lotes
 * História 02: Transformação do Catálogo em Catálogo de Lotes
 * 
 * Endpoints públicos (sem autenticação) para navegação no catálogo
 * Separado do LoteController para evitar conflitos de mapeamento
 */
@RestController
@RequestMapping("/catalogo")
@RequiredArgsConstructor
@Slf4j
public class CatalogoLoteController {

    private final LoteService loteService;
    private final ProdutoService produtoService;

    /**
     * Endpoint principal para catálogo público de lotes
     * GET /api/catalogo/lotes
     */
    @GetMapping("/lotes")
    public ResponseEntity<ApiResponse<Page<LoteDto>>> buscarCatalogoPublico(
            @RequestParam(required = false) String termo,
            @RequestParam(required = false) String categoria,
            @RequestParam(defaultValue = "proximidade_encerramento") String ordenacao,
            @PageableDefault(size = 10) Pageable pageable) {
        
        log.info("Buscando catálogo público de lotes - termo: {}, categoria: {}, ordenacao: {}", 
                termo, categoria, ordenacao);
        
        Page<LoteDto> lotes = loteService.buscarCatalogoPublico(termo, categoria, ordenacao, pageable);
        
        return ResponseEntity.ok(ApiResponse.success("Lotes encontrados", lotes));
    }

    /**
     * Busca lote específico no catálogo (público)
     * GET /api/catalogo/lotes/{loteId}
     */
    @GetMapping("/lotes/{loteId}")
    public ResponseEntity<ApiResponse<LoteDto>> buscarLotePublico(
            @PathVariable String loteId) {
        
        log.info("Buscando lote público no catálogo: {}", loteId);
        
        LoteDto lote = loteService.buscarPorId(loteId);
        
        return ResponseEntity.ok(ApiResponse.success("Lote encontrado", lote));
    }

    /**
     * Endpoint para lotes em destaque (encerrando em 1 semana)
     * GET /api/catalogo/lotes/destaque
     */
    @GetMapping("/lotes/destaque")
    public ResponseEntity<ApiResponse<List<LoteDto>>> buscarLotesDestaque() {
        
        log.info("Buscando lotes em destaque");
        
        List<LoteDto> lotes = loteService.buscarLotesDestaque();
        
        return ResponseEntity.ok(ApiResponse.success("Lotes em destaque", lotes));
    }

    /**
     * Lista categorias disponíveis
     * GET /api/catalogo/categorias
     * Migrado do antigo CatalogoController
     */
    @GetMapping("/categorias")
    public ResponseEntity<ApiResponse<List<String>>> listarCategorias() {
        
        log.info("Listando categorias disponíveis");
        
        List<String> categorias = produtoService.listarCategoriasAtivas();
        
        return ResponseEntity.ok(ApiResponse.success("Categorias disponíveis", categorias));
    }

    /**
     * Busca lotes no catálogo público (método alternativo para compatibilidade)
     * GET /api/catalogo
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<LoteDto>>> buscarLotes(
            @RequestParam(required = false) String termo,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Buscando lotes no catálogo público (compatibilidade) - termo: {}", termo);
        
        Page<LoteDto> lotes = loteService.listarLotesPublicos(termo, pageable);
        
        return ResponseEntity.ok(ApiResponse.success("Lotes encontrados", lotes));
    }
}