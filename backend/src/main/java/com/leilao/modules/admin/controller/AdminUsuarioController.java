package com.leilao.modules.admin.controller;

import com.leilao.modules.admin.dto.AdminUsuarioDto;
import com.leilao.modules.admin.dto.AdminUsuarioUpdateRequest;
import com.leilao.modules.admin.dto.UsuarioSugestaoDto;
import com.leilao.modules.admin.service.AdminUsuarioService;
import com.leilao.shared.dto.ApiResponse;
import com.leilao.shared.enums.UserRole;
import com.leilao.shared.enums.UserStatus;
import com.leilao.shared.util.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * Controller para operações administrativas com usuários
 * FASE 1 - Reorganização de Rotas: Movido para /api/admin/usuarios/**
 * 
 * História 2: Processo de Contratação de Vendedores
 * Requer autenticação e role ADMIN
 */
@RestController
@RequestMapping("/api/admin/usuarios")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUsuarioController {

    private final AdminUsuarioService adminUsuarioService;

    /**
     * Lista todos os usuários com paginação e filtros (para gestão administrativa)
     */
    @GetMapping("/admin-list")
    public ResponseEntity<ApiResponse<Page<AdminUsuarioDto>>> listarUsuariosAdmin(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "nome") String sort,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) UserStatus status,
            @RequestParam(required = false) UserRole role,
            @RequestParam(required = false) Boolean emailVerificado,
            @RequestParam(required = false) Boolean telefoneVerificado,
            @RequestParam(required = false) Boolean isVendedor,
            @RequestParam(required = false) Boolean temContratoAtivo) {
        
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? 
            Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        
        Page<AdminUsuarioDto> usuarios = adminUsuarioService.listarUsuariosAdmin(
            pageable, nome, email, status, role, emailVerificado, 
            telefoneVerificado, isVendedor, temContratoAtivo);
        
        return ResponseEntity.ok(ApiResponse.success(usuarios));
    }

    /**
     * Lista todos os usuários com paginação (para seleção em contratos)
     */
    @GetMapping("/listar")
    public ResponseEntity<ApiResponse<Page<UsuarioSugestaoDto>>> listarUsuarios(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String termo,
            @RequestParam(required = false) String tipo) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("nome").ascending());
        Page<UsuarioSugestaoDto> usuarios = adminUsuarioService.listarUsuarios(pageable, termo, tipo);
        return ResponseEntity.ok(ApiResponse.success(usuarios));
    }

    /**
     * Busca usuários por termo (nome ou email)
     * Usado para seleção de usuários para ativação como vendedores (método antigo - manter compatibilidade)
     */
    @GetMapping("/buscar")
    public ResponseEntity<ApiResponse<List<UsuarioSugestaoDto>>> buscarUsuarios(
            @RequestParam String termo,
            @RequestParam(defaultValue = "10") int limit) {
        
        List<UsuarioSugestaoDto> usuarios = adminUsuarioService.buscarUsuariosPorTermo(termo, limit);
        return ResponseEntity.ok(ApiResponse.success(usuarios));
    }

    /**
     * Busca usuário por ID
     */
    @GetMapping("/{usuarioId}")
    public ResponseEntity<ApiResponse<AdminUsuarioDto>> buscarUsuario(@PathVariable String usuarioId) {
        AdminUsuarioDto usuario = adminUsuarioService.buscarUsuarioPorId(usuarioId);
        return ResponseEntity.ok(ApiResponse.success(usuario));
    }

    /**
     * Atualiza dados do usuário
     */
    @PutMapping("/{usuarioId}")
    public ResponseEntity<ApiResponse<AdminUsuarioDto>> atualizarUsuario(
            @PathVariable String usuarioId,
            @Valid @RequestBody AdminUsuarioUpdateRequest request) {
        
        AdminUsuarioDto usuario = adminUsuarioService.atualizarUsuario(usuarioId, request);
        String message = MessageUtils.getMessage("user.updated");
        return ResponseEntity.ok(ApiResponse.success(message, usuario));
    }

    /**
     * Ativa usuário
     */
    @PatchMapping("/{usuarioId}/ativar")
    public ResponseEntity<ApiResponse<AdminUsuarioDto>> ativarUsuario(@PathVariable String usuarioId) {
        AdminUsuarioDto usuario = adminUsuarioService.ativarUsuario(usuarioId);
        String message = MessageUtils.getMessage("user.activated");
        return ResponseEntity.ok(ApiResponse.success(message, usuario));
    }

    /**
     * Desativa usuário
     */
    @PatchMapping("/{usuarioId}/desativar")
    public ResponseEntity<ApiResponse<AdminUsuarioDto>> desativarUsuario(@PathVariable String usuarioId) {
        AdminUsuarioDto usuario = adminUsuarioService.desativarUsuario(usuarioId);
        String message = MessageUtils.getMessage("user.deactivated");
        return ResponseEntity.ok(ApiResponse.success(message, usuario));
    }

    /**
     * Bloqueia usuário
     */
    @PatchMapping("/{usuarioId}/bloquear")
    public ResponseEntity<ApiResponse<AdminUsuarioDto>> bloquearUsuario(
            @PathVariable String usuarioId,
            @RequestBody(required = false) Map<String, String> body) {
        
        String motivo = body != null ? body.get("motivo") : null;
        AdminUsuarioDto usuario = adminUsuarioService.bloquearUsuario(usuarioId, motivo);
        String message = MessageUtils.getMessage("user.blocked");
        return ResponseEntity.ok(ApiResponse.success(message, usuario));
    }

    /**
     * Desbloqueia usuário
     */
    @PatchMapping("/{usuarioId}/desbloquear")
    public ResponseEntity<ApiResponse<AdminUsuarioDto>> desbloquearUsuario(@PathVariable String usuarioId) {
        AdminUsuarioDto usuario = adminUsuarioService.desbloquearUsuario(usuarioId);
        String message = MessageUtils.getMessage("user.unblocked");
        return ResponseEntity.ok(ApiResponse.success(message, usuario));
    }

    /**
     * Verifica email do usuário
     */
    @PatchMapping("/{usuarioId}/verificar-email")
    public ResponseEntity<ApiResponse<AdminUsuarioDto>> verificarEmail(@PathVariable String usuarioId) {
        AdminUsuarioDto usuario = adminUsuarioService.verificarEmail(usuarioId);
        String message = MessageUtils.getMessage("user.email.verified");
        return ResponseEntity.ok(ApiResponse.success(message, usuario));
    }

    /**
     * Verifica telefone do usuário
     */
    @PatchMapping("/{usuarioId}/verificar-telefone")
    public ResponseEntity<ApiResponse<AdminUsuarioDto>> verificarTelefone(@PathVariable String usuarioId) {
        AdminUsuarioDto usuario = adminUsuarioService.verificarTelefone(usuarioId);
        String message = MessageUtils.getMessage("user.phone.verified");
        return ResponseEntity.ok(ApiResponse.success(message, usuario));
    }

    /**
     * Adiciona role ao usuário
     */
    @PatchMapping("/{usuarioId}/roles/adicionar")
    public ResponseEntity<ApiResponse<AdminUsuarioDto>> adicionarRole(
            @PathVariable String usuarioId,
            @RequestBody Map<String, String> body) {
        
        UserRole role = UserRole.valueOf(body.get("role"));
        AdminUsuarioDto usuario = adminUsuarioService.adicionarRole(usuarioId, role);
        String message = MessageUtils.getMessage("user.role.added");
        return ResponseEntity.ok(ApiResponse.success(message, usuario));
    }

    /**
     * Remove role do usuário
     */
    @PatchMapping("/{usuarioId}/roles/remover")
    public ResponseEntity<ApiResponse<AdminUsuarioDto>> removerRole(
            @PathVariable String usuarioId,
            @RequestBody Map<String, String> body) {
        
        UserRole role = UserRole.valueOf(body.get("role"));
        AdminUsuarioDto usuario = adminUsuarioService.removerRole(usuarioId, role);
        String message = MessageUtils.getMessage("user.role.removed");
        return ResponseEntity.ok(ApiResponse.success(message, usuario));
    }

    /**
     * Redefine senha do usuário
     */
    @PatchMapping("/{usuarioId}/redefinir-senha")
    public ResponseEntity<ApiResponse<Map<String, String>>> redefinirSenha(@PathVariable String usuarioId) {
        String novaSenha = adminUsuarioService.redefinirSenha(usuarioId);
        String message = MessageUtils.getMessage("user.password.reset");
        String newPasswordLabel = MessageUtils.getMessage("admin.user.new.password");
        return ResponseEntity.ok(ApiResponse.success(message, Map.of(newPasswordLabel, novaSenha)));
    }

    /**
     * Obtém estatísticas de usuários
     */
    @GetMapping("/estatisticas")
    public ResponseEntity<ApiResponse<Map<String, Object>>> obterEstatisticas() {
        Map<String, Object> estatisticas = adminUsuarioService.obterEstatisticas();
        String message = MessageUtils.getMessage("statistics.success");
        return ResponseEntity.ok(ApiResponse.success(message, estatisticas));
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