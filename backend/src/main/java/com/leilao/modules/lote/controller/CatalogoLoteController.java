package com.leilao.modules.lote.controller;

import com.leilao.modules.lote.dto.LoteDto;
import com.leilao.modules.lote.service.LoteService;
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
 */
@RestController
@RequestMapping("/api/lotes")
@RequiredArgsConstructor
@Slf4j
public class CatalogoLoteController {

    private final LoteService loteService;

    /**
     * Endpoint principal para catálogo público de lotes
     * História 02: GET /api/lotes/catalogo-publico
     */
    @GetMapping("/catalogo-publico")
    public ResponseEntity<ApiResponse<Page<LoteDto>>> buscarCatalogoPublico(
            @RequestParam(required = false) String termo,
            @RequestParam(required = false) String categoria,
            @RequestParam(defaultValue = "proximidade_encerramento") String ordenacao,
            @PageableDefault(size = 10) Pageable pageable) {
        
        log.info("Buscando catálogo público de lotes - termo: {}, categoria: {}, ordenacao: {}", 
                termo, categoria, ordenacao);
        
        Page<LoteDto> lotes = loteService.buscarCatalogoPublico(termo, categoria, ordenacao, pageable);
        
        return ResponseEntity.ok(ApiResponse.success(lotes));
    }

    /**
     * Endpoint para lotes em destaque (encerrando em 1 semana)
     * História 02: GET /api/lotes/destaque
     */
    @GetMapping("/destaque")
    public ResponseEntity<ApiResponse<List<LoteDto>>> buscarLotesDestaque() {
        
        log.info("Buscando lotes em destaque");
        
        List<LoteDto> lotes = loteService.buscarLotesDestaque();
        
        return ResponseEntity.ok(ApiResponse.success(lotes));
    }

    /**
     * Busca lotes no catálogo público (método original mantido para compatibilidade)
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<LoteDto>>> buscarLotes(
            @RequestParam(required = false) String termo,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Buscando lotes no catálogo público - termo: {}", termo);
        
        Page<LoteDto> lotes = loteService.listarLotesPublicos(termo, pageable);
        
        return ResponseEntity.ok(ApiResponse.success(lotes));
    }

    /**
     * Busca lote específico no catálogo (público)
     */
    @GetMapping("/{loteId}")
    public ResponseEntity<ApiResponse<LoteDto>> buscarLote(
            @PathVariable String loteId) {
        
        log.info("Buscando lote no catálogo: {}", loteId);
        
        LoteDto lote = loteService.buscarPorId(loteId);
        
        return ResponseEntity.ok(ApiResponse.success(lote));
    }
}