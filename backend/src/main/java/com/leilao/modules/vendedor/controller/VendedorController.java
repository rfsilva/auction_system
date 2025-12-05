package com.leilao.modules.vendedor.controller;

import com.leilao.modules.vendedor.dto.VendedorDto;
import com.leilao.modules.vendedor.service.VendedorService;
import com.leilao.shared.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para operações com vendedores
 * FASE 1 - Reorganização de Rotas: Movido para /api/admin/vendedores/**
 * 
 * Requer autenticação e role ADMIN
 */
@RestController
@RequestMapping("/api/admin/vendedores")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class VendedorController {

    private final VendedorService vendedorService;

    /**
     * Lista todos os vendedores
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<VendedorDto>>> listarVendedores(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(defaultValue = "companyName") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) 
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        
        Page<VendedorDto> vendedores = vendedorService.listarTodos(pageable);
        
        return ResponseEntity.ok(ApiResponse.success(vendedores));
    }

    /**
     * Lista vendedores ativos (para seleção em formulários)
     */
    @GetMapping("/ativos")
    public ResponseEntity<ApiResponse<List<VendedorDto>>> listarVendedoresAtivos() {
        List<VendedorDto> vendedores = vendedorService.listarAtivos();
        return ResponseEntity.ok(ApiResponse.success(vendedores));
    }

    /**
     * Busca vendedor por ID
     */
    @GetMapping("/{vendedorId}")
    public ResponseEntity<ApiResponse<VendedorDto>> buscarVendedor(@PathVariable String vendedorId) {
        VendedorDto vendedor = vendedorService.buscarPorId(vendedorId);
        return ResponseEntity.ok(ApiResponse.success(vendedor));
    }
}