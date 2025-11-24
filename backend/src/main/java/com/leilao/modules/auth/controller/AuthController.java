package com.leilao.modules.auth.controller;

import com.leilao.shared.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller para autenticação e autorização
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login() {
        // TODO: Implementar login
        return ResponseEntity.ok(ApiResponse.success("Login endpoint - TODO"));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register() {
        // TODO: Implementar registro
        return ResponseEntity.ok(ApiResponse.success("Register endpoint - TODO"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<String>> refresh() {
        // TODO: Implementar refresh token
        return ResponseEntity.ok(ApiResponse.success("Refresh endpoint - TODO"));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout() {
        // TODO: Implementar logout
        return ResponseEntity.ok(ApiResponse.success("Logout endpoint - TODO"));
    }
}