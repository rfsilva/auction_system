package com.leilao.modules.auth.controller;

import com.leilao.modules.auth.dto.*;
import com.leilao.modules.auth.service.AuthService;
import com.leilao.shared.dto.ApiResponse;
import com.leilao.shared.util.MessageUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Controller para autenticação e autorização
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
@Validated
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    /**
     * Endpoint para login
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        try {
            log.info("Tentativa de login para email: {}", request.getEmail());
            AuthResponse response = authService.login(request);
            String message = MessageUtils.getMessage("auth.login.success");
            return ResponseEntity.ok(ApiResponse.success(message, response));
        } catch (Exception e) {
            log.error("Erro no login para email {}: {}", request.getEmail(), e.getMessage());
            String errorMessage = MessageUtils.getMessage("auth.login.failed.message");
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(errorMessage, e.getMessage()));
        }
    }

    /**
     * Endpoint para registro
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        try {
            log.info("Tentativa de registro para email: {}", request.getEmail());
            AuthResponse response = authService.register(request);
            String message = MessageUtils.getMessage("auth.register.success");
            return ResponseEntity.ok(ApiResponse.success(message, response));
        } catch (Exception e) {
            log.error("Erro no registro para email {}: {}", request.getEmail(), e.getMessage());
            String errorMessage = MessageUtils.getMessage("auth.register.failed");
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(errorMessage, e.getMessage()));
        }
    }

    /**
     * Endpoint para refresh token
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        try {
            AuthResponse response = authService.refreshToken(request);
            String message = MessageUtils.getMessage("auth.refresh.success");
            return ResponseEntity.ok(ApiResponse.success(message, response));
        } catch (Exception e) {
            log.error("Erro ao renovar token: {}", e.getMessage());
            String errorMessage = MessageUtils.getMessage("auth.refresh.failed");
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(errorMessage, e.getMessage()));
        }
    }

    /**
     * Endpoint para verificar se email existe
     */
    @GetMapping("/check-email")
    public ResponseEntity<ApiResponse<Boolean>> checkEmail(
            @RequestParam 
            @NotBlank(message = "Email é obrigatório") 
            @Email(message = "Email deve ter formato válido") 
            String email) {
        try {
            boolean exists = authService.checkEmailExists(email);
            return ResponseEntity.ok(ApiResponse.success(exists));
        } catch (Exception e) {
            log.error("Erro ao verificar email {}: {}", email, e.getMessage());
            String errorMessage = MessageUtils.getMessage("auth.email.check.error");
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(errorMessage, e.getMessage()));
        }
    }

    /**
     * Endpoint de health check
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        String message = MessageUtils.getMessage("auth.service.running");
        return ResponseEntity.ok(ApiResponse.success(message));
    }

    /**
     * Endpoint para logout
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(HttpServletRequest request) {
        try {
            // TODO: Implementar invalidação de token (blacklist)
            log.info("Logout realizado");
            String message = MessageUtils.getMessage("auth.logout.success");
            return ResponseEntity.ok(ApiResponse.success(message));
        } catch (Exception e) {
            log.error("Erro no logout: {}", e.getMessage());
            String errorMessage = MessageUtils.getMessage("auth.logout.error");
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(errorMessage, e.getMessage()));
        }
    }
}