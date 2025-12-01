package com.leilao.modules.admin.controller;

import com.leilao.modules.admin.dto.UsuarioSugestaoDto;
import com.leilao.modules.admin.service.AdminUsuarioService;
import com.leilao.shared.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para operações administrativas com usuários
 * História 2: Processo de Contratação de Vendedores
 */
@RestController
@RequestMapping("/admin/usuarios")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUsuarioController {

    private final AdminUsuarioService adminUsuarioService;

    /**
     * Busca usuários por termo (nome ou email)
     * Usado para seleção de usuários para ativação como vendedores
     */
    @GetMapping("/buscar")
    public ResponseEntity<ApiResponse<List<UsuarioSugestaoDto>>> buscarUsuarios(
            @RequestParam String termo,
            @RequestParam(defaultValue = "10") int limit) {
        
        List<UsuarioSugestaoDto> usuarios = adminUsuarioService.buscarUsuariosPorTermo(termo, limit);
        return ResponseEntity.ok(ApiResponse.success(usuarios));
    }

    /**
     * Verifica se usuário pode ser ativado como vendedor
     */
    @GetMapping("/{usuarioId}/pode-ser-vendedor")
    public ResponseEntity<ApiResponse<Boolean>> podeSerVendedor(@PathVariable String usuarioId) {
        boolean podeSerVendedor = adminUsuarioService.podeSerVendedor(usuarioId);
        return ResponseEntity.ok(ApiResponse.success(podeSerVendedor));
    }
}