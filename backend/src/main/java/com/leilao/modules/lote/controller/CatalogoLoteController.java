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

/**
 * Controller público para catálogo de lotes
 */
@RestController
@RequestMapping("/catalogo/lotes")
@RequiredArgsConstructor
@Slf4j
public class CatalogoLoteController {

    private final LoteService loteService;

    /**
     * Busca lotes no catálogo público
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