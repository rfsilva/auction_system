package com.leilao.modules.contrato.controller;

import com.leilao.modules.auth.entity.Usuario;
import com.leilao.modules.contrato.dto.ContratoDto;
import com.leilao.modules.contrato.service.ContratoService;
import com.leilao.shared.dto.ApiResponse;
import com.leilao.shared.util.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para operações de contratos na área do vendedor
 * FASE 1 - Reorganização de Rotas: Endpoints para /api/vendedor/contratos/**
 * 
 * Permite que vendedores consultem seus próprios contratos
 */
@RestController
@RequestMapping("/api/vendedor/contratos")
@RequiredArgsConstructor
@Slf4j
public class VendedorContratoController {

    private final ContratoService contratoService;

    /**
     * Lista contratos ativos do vendedor atual (para seleção em lotes)
     * Endpoint acessível por vendedores
     */
    @GetMapping("/meus-ativos")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ApiResponse<List<ContratoDto>>> listarMeusContratosAtivos(
            @AuthenticationPrincipal Usuario usuario) {
        
        log.info("Vendedor {} listando seus contratos ativos", usuario.getId());
        
        List<ContratoDto> contratos = contratoService.listarContratosAtivosDoVendedor(usuario.getId());
        String message = MessageUtils.getMessage("contract.active.list.found");
        
        return ResponseEntity.ok(ApiResponse.success(message, contratos));
    }

    /**
     * Busca contrato ativo do vendedor para uma categoria específica
     */
    @GetMapping("/meu-ativo")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ApiResponse<ContratoDto>> buscarMeuContratoAtivo(
            @RequestParam(required = false) String categoria,
            @AuthenticationPrincipal Usuario usuario) {
        
        log.info("Vendedor {} buscando contrato ativo para categoria: {}", usuario.getId(), categoria);
        
        ContratoDto contrato = contratoService.buscarContratoAtivoParaCategoria(usuario.getId(), categoria);
        
        if (contrato != null) {
            String message = MessageUtils.getMessage("contract.active.found");
            return ResponseEntity.ok(ApiResponse.success(message, contrato));
        } else {
            String message = MessageUtils.getMessage("contract.active.not.found");
            return ResponseEntity.ok(ApiResponse.success(message, null));
        }
    }

    /**
     * Verifica se o vendedor tem contrato ativo
     */
    @GetMapping("/tenho-ativo")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ApiResponse<Boolean>> tenhoContratoAtivo(
            @AuthenticationPrincipal Usuario usuario) {
        
        log.info("Vendedor {} verificando se tem contrato ativo", usuario.getId());
        
        boolean temAtivo = contratoService.vendedorTemContratoAtivo(usuario.getId());
        String message = MessageUtils.getMessage("contract.status.verified");
        
        return ResponseEntity.ok(ApiResponse.success(message, temAtivo));
    }
}