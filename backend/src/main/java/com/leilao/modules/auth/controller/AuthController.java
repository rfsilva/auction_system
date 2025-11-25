package com.leilao.modules.auth.controller;

import com.leilao.modules.auth.dto.*;
import com.leilao.modules.auth.service.AuthService;
import com.leilao.shared.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller para autenticação e autorização
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    /**
     * Endpoint para login
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        try {
            logger.info("Tentativa de login para email: {}", request.getEmail());
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(ApiResponse.success("Login realizado com sucesso", response));
        } catch (Exception e) {
            logger.error("Erro no login para email {}: {}", request.getEmail(), e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Falha no login", e.getMessage()));
        }
    }

    /**
     * Endpoint para registro
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        try {
            logger.info("Tentativa de registro para email: {}", request.getEmail());
            AuthResponse response = authService.register(request);
            return ResponseEntity.ok(ApiResponse.success("Usuário registrado com sucesso", response));
        } catch (Exception e) {
            logger.error("Erro no registro para email {}: {}", request.getEmail(), e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Falha no registro", e.getMessage()));
        }
    }

    /**
     * Endpoint para refresh token
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        try {
            AuthResponse response = authService.refreshToken(request);
            return ResponseEntity.ok(ApiResponse.success("Token renovado com sucesso", response));
        } catch (Exception e) {
            logger.error("Erro ao renovar token: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Falha ao renovar token", e.getMessage()));
        }
    }

    /**
     * Endpoint para verificar se email existe
     */
    @GetMapping("/check-email")
    public ResponseEntity<ApiResponse<Boolean>> checkEmail(@RequestParam String email) {
        try {
            boolean exists = authService.checkEmailExists(email);
            return ResponseEntity.ok(ApiResponse.success(exists));
        } catch (Exception e) {
            logger.error("Erro ao verificar email {}: {}", email, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Erro ao verificar email", e.getMessage()));
        }
    }

    /**
     * Endpoint de health check
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(ApiResponse.success("Auth service is running"));
    }

    /**
     * Endpoint para logout
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(HttpServletRequest request) {
        try {
            // TODO: Implementar invalidação de token (blacklist)
            logger.info("Logout realizado");
            return ResponseEntity.ok(ApiResponse.success("Logout realizado com sucesso"));
        } catch (Exception e) {
            logger.error("Erro no logout: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Erro no logout", e.getMessage()));
        }
    }
}