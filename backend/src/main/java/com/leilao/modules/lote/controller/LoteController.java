package com.leilao.modules.lote.controller;

import com.leilao.modules.auth.entity.Usuario;
import com.leilao.modules.lote.dto.LoteCreateRequest;
import com.leilao.modules.lote.dto.LoteDto;
import com.leilao.modules.lote.dto.LoteUpdateRequest;
import com.leilao.modules.lote.service.LoteService;
import com.leilao.shared.dto.ApiResponse;
import com.leilao.shared.util.MessageUtils;
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
 * Controller para operações privadas de Lote (área do vendedor)
 * FASE 1 - Reorganização de Rotas: Movido para /api/vendedor/lotes/**
 * 
 * Endpoints para vendedores gerenciarem seus lotes
 * Requer autenticação e role SELLER
 */
@RestController
@RequestMapping("/api/vendedor/lotes")
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
        String message = MessageUtils.getMessage("lot.created");
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(message, lote));
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
        String message = MessageUtils.getMessage("lot.updated");
        
        return ResponseEntity.ok(ApiResponse.success(message, lote));
    }

    /**
     * Busca lote por ID (área privada do vendedor)
     */
    @GetMapping("/{loteId}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ApiResponse<LoteDto>> buscarLotePrivado(
            @PathVariable String loteId,
            @AuthenticationPrincipal Usuario usuario) {
        
        log.info("Buscando lote privado: {} para vendedor: {}", loteId, usuario.getId());
        
        LoteDto lote = loteService.buscarPorId(loteId);
        String message = MessageUtils.getMessage("lot.found");
        
        return ResponseEntity.ok(ApiResponse.success(message, lote));
    }

    /**
     * Lista lotes do vendedor logado
     */
    @GetMapping
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ApiResponse<Page<LoteDto>>> listarMeusLotes(
            @AuthenticationPrincipal Usuario usuario,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Listando lotes do vendedor: {}", usuario.getId());
        
        Page<LoteDto> lotes = loteService.listarLotesVendedor(usuario.getId(), pageable);
        String message = MessageUtils.getMessage("lot.seller.list");
        
        return ResponseEntity.ok(ApiResponse.success(message, lotes));
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
        String message = MessageUtils.getMessage("lot.activated");
        
        return ResponseEntity.ok(ApiResponse.success(message, lote));
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
        String message = MessageUtils.getMessage("lot.cancelled");
        
        return ResponseEntity.ok(ApiResponse.success(message, lote));
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
        String message = MessageUtils.getMessage("lot.deleted");
        
        return ResponseEntity.ok(ApiResponse.success(message, null));
    }
}