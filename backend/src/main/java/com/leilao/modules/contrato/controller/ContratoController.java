package com.leilao.modules.contrato.controller;

import com.leilao.modules.auth.entity.Usuario;
import com.leilao.modules.contrato.dto.*;
import com.leilao.modules.contrato.service.ContratoService;
import com.leilao.shared.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
 * Apenas administradores podem gerenciar contratos
 */
@RestController
@RequestMapping("/contratos")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class ContratoController {

    private final ContratoService contratoService;

    /**
     * Cria um novo contrato
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ContratoDto>> criarContrato(
            @Valid @RequestBody ContratoCreateRequest request,
            @AuthenticationPrincipal Usuario admin) {
        
        log.info("Admin {} criando contrato para vendedor {}", admin.getId(), request.getSellerId());
        
        ContratoDto contrato = contratoService.criarContrato(request, admin.getId());
        
        return ResponseEntity.ok(ApiResponse.success("Contrato criado com sucesso", contrato));
    }

    /**
     * Atualiza um contrato existente
     */
    @PutMapping("/{contratoId}")
    public ResponseEntity<ApiResponse<ContratoDto>> atualizarContrato(
            @PathVariable String contratoId,
            @Valid @RequestBody ContratoUpdateRequest request,
            @AuthenticationPrincipal Usuario admin) {
        
        log.info("Admin {} atualizando contrato {}", admin.getId(), contratoId);
        
        ContratoDto contrato = contratoService.atualizarContrato(contratoId, request, admin.getId());
        
        return ResponseEntity.ok(ApiResponse.success("Contrato atualizado com sucesso", contrato));
    }

    /**
     * Busca contrato por ID
     */
    @GetMapping("/{contratoId}")
    public ResponseEntity<ApiResponse<ContratoDto>> buscarContrato(
            @PathVariable String contratoId) {
        
        ContratoDto contrato = contratoService.buscarPorId(contratoId);
        
        return ResponseEntity.ok(ApiResponse.success(contrato));
    }

    /**
     * Lista contratos com filtros
     */
    @GetMapping
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
    public ResponseEntity<ApiResponse<ContratoDto>> ativarContrato(
            @PathVariable String contratoId,
            @AuthenticationPrincipal Usuario admin) {
        
        log.info("Admin {} ativando contrato {}", admin.getId(), contratoId);
        
        ContratoDto contrato = contratoService.ativarContrato(contratoId, admin.getId());
        
        return ResponseEntity.ok(ApiResponse.success("Contrato ativado com sucesso", contrato));
    }

    /**
     * Cancela um contrato
     */
    @PostMapping("/{contratoId}/cancelar")
    public ResponseEntity<ApiResponse<ContratoDto>> cancelarContrato(
            @PathVariable String contratoId,
            @AuthenticationPrincipal Usuario admin) {
        
        log.info("Admin {} cancelando contrato {}", admin.getId(), contratoId);
        
        ContratoDto contrato = contratoService.cancelarContrato(contratoId, admin.getId());
        
        return ResponseEntity.ok(ApiResponse.success("Contrato cancelado com sucesso", contrato));
    }

    /**
     * Suspende um contrato
     */
    @PostMapping("/{contratoId}/suspender")
    public ResponseEntity<ApiResponse<ContratoDto>> suspenderContrato(
            @PathVariable String contratoId,
            @AuthenticationPrincipal Usuario admin) {
        
        log.info("Admin {} suspendendo contrato {}", admin.getId(), contratoId);
        
        ContratoDto contrato = contratoService.suspenderContrato(contratoId, admin.getId());
        
        return ResponseEntity.ok(ApiResponse.success("Contrato suspenso com sucesso", contrato));
    }

    /**
     * Exclui um contrato (apenas rascunhos)
     */
    @DeleteMapping("/{contratoId}")
    public ResponseEntity<ApiResponse<Void>> excluirContrato(
            @PathVariable String contratoId,
            @AuthenticationPrincipal Usuario admin) {
        
        log.info("Admin {} excluindo contrato {}", admin.getId(), contratoId);
        
        contratoService.excluirContrato(contratoId, admin.getId());
        
        return ResponseEntity.ok(ApiResponse.success("Contrato excluído com sucesso", null));
    }

    /**
     * Busca contrato ativo para vendedor e categoria
     */
    @GetMapping("/vendedor/{sellerId}/ativo")
    public ResponseEntity<ApiResponse<ContratoDto>> buscarContratoAtivo(
            @PathVariable String sellerId,
            @RequestParam(required = false) String categoria) {
        
        ContratoDto contrato = contratoService.buscarContratoAtivoParaCategoria(sellerId, categoria);
        
        if (contrato != null) {
            return ResponseEntity.ok(ApiResponse.success(contrato));
        } else {
            return ResponseEntity.ok(ApiResponse.success("Nenhum contrato ativo encontrado", null));
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
    public ResponseEntity<ApiResponse<Boolean>> vendedorTemContratoAtivo(@PathVariable String sellerId) {
        boolean temAtivo = contratoService.vendedorTemContratoAtivo(sellerId);
        return ResponseEntity.ok(ApiResponse.success(temAtivo));
    }
}