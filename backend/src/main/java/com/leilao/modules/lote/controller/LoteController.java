package com.leilao.modules.lote.controller;

import com.leilao.modules.auth.entity.Usuario;
import com.leilao.modules.lote.dto.LoteCreateRequest;
import com.leilao.modules.lote.dto.LoteDto;
import com.leilao.modules.lote.dto.LoteUpdateRequest;
import com.leilao.modules.lote.service.LoteService;
import com.leilao.shared.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Controller para operações de Lote
 */
@RestController
@RequestMapping("/lotes")
@RequiredArgsConstructor
@Slf4j
public class LoteController {

    private final LoteService loteService;

    /**
     * Cria um novo lote
     */
    @PostMapping
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ApiResponse<LoteDto>> criarLote(
            @Valid @RequestBody LoteCreateRequest request,
            @AuthenticationPrincipal Usuario usuario) {
        
        log.info("Criando lote para vendedor: {}", usuario.getId());
        
        LoteDto lote = loteService.criarLote(request, usuario.getId());
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Lote criado com sucesso", lote));
    }

    /**
     * Atualiza um lote existente
     */
    @PutMapping("/{loteId}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ApiResponse<LoteDto>> atualizarLote(
            @PathVariable String loteId,
            @Valid @RequestBody LoteUpdateRequest request,
            @AuthenticationPrincipal Usuario usuario) {
        
        log.info("Atualizando lote: {} para vendedor: {}", loteId, usuario.getId());
        
        LoteDto lote = loteService.atualizarLote(loteId, request, usuario.getId());
        
        return ResponseEntity.ok(ApiResponse.success("Lote atualizado com sucesso", lote));
    }

    /**
     * Busca lote por ID
     */
    @GetMapping("/{loteId}")
    public ResponseEntity<ApiResponse<LoteDto>> buscarLote(
            @PathVariable String loteId) {
        
        LoteDto lote = loteService.buscarPorId(loteId);
        
        return ResponseEntity.ok(ApiResponse.success(lote));
    }

    /**
     * Lista lotes do vendedor logado
     */
    @GetMapping("/meus-lotes")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ApiResponse<Page<LoteDto>>> listarMeusLotes(
            @AuthenticationPrincipal Usuario usuario,
            @PageableDefault(size = 20) Pageable pageable) {
        
        Page<LoteDto> lotes = loteService.listarLotesVendedor(usuario.getId(), pageable);
        
        return ResponseEntity.ok(ApiResponse.success(lotes));
    }

    /**
     * Lista lotes públicos (para catálogo)
     */
    @GetMapping("/publicos")
    public ResponseEntity<ApiResponse<Page<LoteDto>>> listarLotesPublicos(
            @RequestParam(required = false) String termo,
            @PageableDefault(size = 20) Pageable pageable) {
        
        Page<LoteDto> lotes = loteService.listarLotesPublicos(termo, pageable);
        
        return ResponseEntity.ok(ApiResponse.success(lotes));
    }

    /**
     * Ativa um lote
     */
    @PostMapping("/{loteId}/ativar")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ApiResponse<LoteDto>> ativarLote(
            @PathVariable String loteId,
            @AuthenticationPrincipal Usuario usuario) {
        
        log.info("Ativando lote: {} para vendedor: {}", loteId, usuario.getId());
        
        LoteDto lote = loteService.ativarLote(loteId, usuario.getId());
        
        return ResponseEntity.ok(ApiResponse.success("Lote ativado com sucesso", lote));
    }

    /**
     * Cancela um lote
     */
    @PostMapping("/{loteId}/cancelar")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ApiResponse<LoteDto>> cancelarLote(
            @PathVariable String loteId,
            @AuthenticationPrincipal Usuario usuario) {
        
        log.info("Cancelando lote: {} para vendedor: {}", loteId, usuario.getId());
        
        LoteDto lote = loteService.cancelarLote(loteId, usuario.getId());
        
        return ResponseEntity.ok(ApiResponse.success("Lote cancelado com sucesso", lote));
    }

    /**
     * Exclui um lote
     */
    @DeleteMapping("/{loteId}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ApiResponse<Void>> excluirLote(
            @PathVariable String loteId,
            @AuthenticationPrincipal Usuario usuario) {
        
        log.info("Excluindo lote: {} para vendedor: {}", loteId, usuario.getId());
        
        loteService.excluirLote(loteId, usuario.getId());
        
        return ResponseEntity.ok(ApiResponse.success("Lote excluído com sucesso", null));
    }
}