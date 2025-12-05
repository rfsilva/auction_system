package com.leilao.modules.contrato.controller;

import com.leilao.modules.auth.entity.Usuario;
import com.leilao.modules.contrato.dto.*;
import com.leilao.modules.contrato.service.ContratoService;
import com.leilao.shared.dto.ApiResponse;
import com.leilao.shared.util.MessageUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para operações com contratos
 * FASE 1 - Reorganização de Rotas: Movido para /api/admin/contratos/**
 * 
 * Apenas administradores podem gerenciar contratos
 * Inclui funcionalidades da História 2: Processo de Contratação de Vendedores
 */
@RestController
@RequestMapping("/api/admin/contratos")
@RequiredArgsConstructor
@Slf4j
public class ContratoController {

    private final ContratoService contratoService;

    /**
     * Cria um novo contrato a partir de usuário (novo fluxo)
     * Promove automaticamente o usuário a vendedor se necessário
     */
    @PostMapping("/criar-de-usuario")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ContratoDto>> criarContratoDeUsuario(
            @Valid @RequestBody ContratoCreateFromUserRequest request,
            @AuthenticationPrincipal Usuario admin) {
        
        log.info("Admin {} criando contrato para usuário {}", admin.getId(), request.getUsuarioId());
        
        ContratoDto contrato = contratoService.criarContratoDeUsuario(request, admin.getId());
        
        String message = Boolean.TRUE.equals(request.getAtivarImediatamente())
                ? "Contrato criado e ativado com sucesso. Usuário promovido a vendedor ativo."
                : "Contrato criado em rascunho. Usuário promovido a vendedor. Ative quando necessário.";
        
        return ResponseEntity.ok(ApiResponse.success(message, contrato));
    }

    /**
     * Cria um novo contrato (fluxo antigo - para vendedores existentes)
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ContratoDto>> criarContrato(
            @Valid @RequestBody ContratoCreateRequest request,
            @AuthenticationPrincipal Usuario admin) {
        
        log.info("Admin {} criando contrato para vendedor {}", admin.getId(), request.getSellerId());
        
        ContratoDto contrato = contratoService.criarContrato(request, admin.getId());
        String message = MessageUtils.getMessage("contract.created");
        
        return ResponseEntity.ok(ApiResponse.success(message, contrato));
    }

    /**
     * Ativa usuário como vendedor através de contrato
     * História 2: Processo de Contratação de Vendedores
     */
    @PostMapping("/ativar-vendedor")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ContratoDto>> ativarVendedor(
            @Valid @RequestBody AtivarVendedorRequest request,
            @AuthenticationPrincipal Usuario admin) {
        
        log.info("Admin {} ativando usuário {} como vendedor", admin.getId(), request.getUsuarioId());
        
        ContratoDto contrato = contratoService.ativarVendedor(request, admin.getId());
        
        String message = request.getAtivarImediatamente() 
                ? "Usuário transformado em vendedor ativo com sucesso"
                : "Contrato criado em rascunho. Ative quando necessário";
        
        return ResponseEntity.ok(ApiResponse.success(message, contrato));
    }

    /**
     * Atualiza um contrato existente
     */
    @PutMapping("/{contratoId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ContratoDto>> atualizarContrato(
            @PathVariable String contratoId,
            @Valid @RequestBody ContratoUpdateRequest request,
            @AuthenticationPrincipal Usuario admin) {
        
        log.info("Admin {} atualizando contrato {}", admin.getId(), contratoId);
        
        ContratoDto contrato = contratoService.atualizarContrato(contratoId, request, admin.getId());
        String message = MessageUtils.getMessage("contract.updated");
        
        return ResponseEntity.ok(ApiResponse.success(message, contrato));
    }

    /**
     * Busca contrato por ID
     */
    @GetMapping("/{contratoId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ContratoDto>> buscarContrato(
            @PathVariable String contratoId) {
        
        ContratoDto contrato = contratoService.buscarPorId(contratoId);
        
        return ResponseEntity.ok(ApiResponse.success(contrato));
    }

    /**
     * Lista contratos com filtros
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<ContratoDto>>> listarContratos(
            @RequestParam(required = false) String sellerId,
            @RequestParam(required = false) String sellerName,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) String feeRateMin,
            @RequestParam(required = false) String feeRateMax,
            @RequestParam(required = false) String termo,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String direction) {
        
        ContratoFiltroRequest filtros = new ContratoFiltroRequest();
        filtros.setSellerId(sellerId);
        filtros.setSellerName(sellerName);
        if (status != null) {
            try {
                filtros.setStatus(com.leilao.shared.enums.ContractStatus.valueOf(status.toUpperCase()));
            } catch (IllegalArgumentException e) {
                // Ignora status inválido
            }
        }
        filtros.setCategoria(categoria);
        filtros.setActive(active);
        if (feeRateMin != null) {
            try {
                filtros.setFeeRateMin(new java.math.BigDecimal(feeRateMin));
            } catch (NumberFormatException e) {
                // Ignora valor inválido
            }
        }
        if (feeRateMax != null) {
            try {
                filtros.setFeeRateMax(new java.math.BigDecimal(feeRateMax));
            } catch (NumberFormatException e) {
                // Ignora valor inválido
            }
        }
        filtros.setTermo(termo);
        filtros.setPage(page);
        filtros.setSize(size);
        filtros.setSort(sort);
        filtros.setDirection(direction);
        
        Page<ContratoDto> contratos = contratoService.listarContratos(filtros);
        
        return ResponseEntity.ok(ApiResponse.success(contratos));
    }

    /**
     * Lista contratos por vendedor
     */
    @GetMapping("/vendedor/{sellerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<ContratoDto>>> listarContratosPorVendedor(
            @PathVariable String sellerId,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        
        Page<ContratoDto> contratos = contratoService.listarContratosPorVendedor(sellerId, pageable);
        
        return ResponseEntity.ok(ApiResponse.success(contratos));
    }

    /**
     * Ativa um contrato
     */
    @PostMapping("/{contratoId}/ativar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ContratoDto>> ativarContrato(
            @PathVariable String contratoId,
            @AuthenticationPrincipal Usuario admin) {
        
        log.info("Admin {} ativando contrato {}", admin.getId(), contratoId);
        
        ContratoDto contrato = contratoService.ativarContrato(contratoId, admin.getId());
        String message = MessageUtils.getMessage("contract.activated");
        
        return ResponseEntity.ok(ApiResponse.success(message, contrato));
    }

    /**
     * Cancela um contrato
     */
    @PostMapping("/{contratoId}/cancelar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ContratoDto>> cancelarContrato(
            @PathVariable String contratoId,
            @AuthenticationPrincipal Usuario admin) {
        
        log.info("Admin {} cancelando contrato {}", admin.getId(), contratoId);
        
        ContratoDto contrato = contratoService.cancelarContrato(contratoId, admin.getId());
        String message = MessageUtils.getMessage("contract.cancelled");
        
        return ResponseEntity.ok(ApiResponse.success(message, contrato));
    }

    /**
     * Suspende um contrato
     */
    @PostMapping("/{contratoId}/suspender")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ContratoDto>> suspenderContrato(
            @PathVariable String contratoId,
            @AuthenticationPrincipal Usuario admin) {
        
        log.info("Admin {} suspendendo contrato {}", admin.getId(), contratoId);
        
        ContratoDto contrato = contratoService.suspenderContrato(contratoId, admin.getId());
        String message = MessageUtils.getMessage("contract.suspended");
        
        return ResponseEntity.ok(ApiResponse.success(message, contrato));
    }

    /**
     * Exclui um contrato (apenas rascunhos)
     */
    @DeleteMapping("/{contratoId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> excluirContrato(
            @PathVariable String contratoId,
            @AuthenticationPrincipal Usuario admin) {
        
        log.info("Admin {} excluindo contrato {}", admin.getId(), contratoId);
        
        contratoService.excluirContrato(contratoId, admin.getId());
        String message = MessageUtils.getMessage("contract.deleted");
        
        return ResponseEntity.ok(ApiResponse.success(message, null));
    }

    /**
     * Busca contrato ativo para vendedor e categoria
     */
    @GetMapping("/vendedor/{sellerId}/ativo")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ContratoDto>> buscarContratoAtivo(
            @PathVariable String sellerId,
            @RequestParam(required = false) String categoria) {
        
        ContratoDto contrato = contratoService.buscarContratoAtivoParaCategoria(sellerId, categoria);
        
        if (contrato != null) {
            String message = MessageUtils.getMessage("contract.active.found");
            return ResponseEntity.ok(ApiResponse.success(message, contrato));
        } else {
            String message = MessageUtils.getMessage("contract.active.not.found");
            return ResponseEntity.ok(ApiResponse.success(message, null));
        }
    }

    /**
     * Lista categorias disponíveis
     */
    @GetMapping("/categorias")
    public ResponseEntity<ApiResponse<List<String>>> listarCategorias() {
        List<String> categorias = contratoService.listarCategorias();
        return ResponseEntity.ok(ApiResponse.success(categorias));
    }

    /**
     * Verifica se vendedor tem contrato ativo
     */
    @GetMapping("/vendedor/{sellerId}/tem-ativo")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Boolean>> vendedorTemContratoAtivo(@PathVariable String sellerId) {
        boolean temAtivo = contratoService.vendedorTemContratoAtivo(sellerId);
        String message = MessageUtils.getMessage("contract.status.verified");
        return ResponseEntity.ok(ApiResponse.success(message, temAtivo));
    }
}