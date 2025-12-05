package com.leilao.modules.test.controller;

import com.leilao.shared.dto.ApiResponse;
import com.leilao.shared.enums.*;
import com.leilao.shared.util.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Controller para testar funcionalidades de i18n usando MessageSourceAccessor
 */
@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final MessageSourceAccessor messageSourceAccessor;

    /**
     * Testa mensagens básicas de i18n
     */
    @GetMapping("/i18n/messages")
    public ResponseEntity<ApiResponse<Map<String, Object>>> testMessages(
            @RequestParam(required = false) String lang) {
        
        Map<String, Object> result = new HashMap<>();
        
        // Locale atual
        Locale currentLocale = LocaleContextHolder.getLocale();
        result.put("currentLocale", currentLocale.toString());
        
        // Teste de mensagens básicas
        result.put("app.name", messageSourceAccessor.getMessage("app.name", currentLocale));
        result.put("app.welcome", messageSourceAccessor.getMessage("app.welcome", currentLocale));
        result.put("auth.login.success", messageSourceAccessor.getMessage("auth.login.success", currentLocale));
        result.put("user.created", messageSourceAccessor.getMessage("user.created", currentLocale));
        result.put("contract.created", messageSourceAccessor.getMessage("contract.created", currentLocale));
        result.put("product.created", messageSourceAccessor.getMessage("product.created", currentLocale));
        
        // Teste com parâmetros
        result.put("email.welcome.body", messageSourceAccessor.getMessage("email.welcome.body", new Object[]{"João"}, currentLocale));
        result.put("validation.min.length", messageSourceAccessor.getMessage("validation.min.length", new Object[]{5}, currentLocale));
        
        String message = MessageUtils.getMessage("test.messages");
        return ResponseEntity.ok(ApiResponse.success(message, result));
    }

    /**
     * Testa mensagens de enums
     */
    @GetMapping("/i18n/enums")
    public ResponseEntity<ApiResponse<Map<String, Object>>> testEnumMessages() {
        
        Map<String, Object> result = new HashMap<>();
        Locale currentLocale = LocaleContextHolder.getLocale();
        
        // Contract Status
        Map<String, String> contractStatus = new HashMap<>();
        for (ContractStatus status : ContractStatus.values()) {
            contractStatus.put(status.name(), messageSourceAccessor.getMessage(status.getDisplayName(), currentLocale));
        }
        result.put("contractStatus", contractStatus);
        
        // User Status
        Map<String, String> userStatus = new HashMap<>();
        for (UserStatus status : UserStatus.values()) {
            userStatus.put(status.name(), messageSourceAccessor.getMessage(status.getDisplayName(), currentLocale));
        }
        result.put("userStatus", userStatus);
        
        // User Roles
        Map<String, String> userRoles = new HashMap<>();
        for (UserRole role : UserRole.values()) {
            userRoles.put(role.name(), messageSourceAccessor.getMessage(role.getDisplayName(), currentLocale));
        }
        result.put("userRoles", userRoles);
        
        // Product Status
        Map<String, String> productStatus = new HashMap<>();
        for (ProdutoStatus status : ProdutoStatus.values()) {
            productStatus.put(status.name(), messageSourceAccessor.getMessage(status.getDisplayName(), currentLocale));
        }
        result.put("productStatus", productStatus);
        
        // Lot Status
        Map<String, String> lotStatus = new HashMap<>();
        for (LoteStatus status : LoteStatus.values()) {
            lotStatus.put(status.name(), messageSourceAccessor.getMessage(status.getDisplayName(), currentLocale));
        }
        result.put("lotStatus", lotStatus);
        
        String message = MessageUtils.getMessage("test.enums");
        return ResponseEntity.ok(ApiResponse.success(message, result));
    }

    /**
     * Testa mensagens em diferentes idiomas
     */
    @GetMapping("/i18n/languages")
    public ResponseEntity<ApiResponse<Map<String, Object>>> testLanguages() {
        
        Map<String, Object> result = new HashMap<>();
        
        String key = "app.welcome";
        
        // Português (padrão)
        result.put("pt", messageSourceAccessor.getMessage(key, new Locale("pt", "BR")));
        
        // Inglês
        result.put("en", messageSourceAccessor.getMessage(key, Locale.ENGLISH));
        
        // Espanhol
        result.put("es", messageSourceAccessor.getMessage(key, new Locale("es", "ES")));
        
        // Italiano
        result.put("it", messageSourceAccessor.getMessage(key, new Locale("it", "IT")));
        
        // Teste com parâmetros
        String emailKey = "email.welcome.body";
        Map<String, String> emailMessages = new HashMap<>();
        emailMessages.put("pt", messageSourceAccessor.getMessage(emailKey, new Object[]{"João"}, new Locale("pt", "BR")));
        emailMessages.put("en", messageSourceAccessor.getMessage(emailKey, new Object[]{"John"}, Locale.ENGLISH));
        emailMessages.put("es", messageSourceAccessor.getMessage(emailKey, new Object[]{"Juan"}, new Locale("es", "ES")));
        emailMessages.put("it", messageSourceAccessor.getMessage(emailKey, new Object[]{"Giovanni"}, new Locale("it", "IT")));
        
        result.put("emailWelcome", emailMessages);
        
        String message = MessageUtils.getMessage("test.languages");
        return ResponseEntity.ok(ApiResponse.success(message, result));
    }

    /**
     * Testa mudança de locale via parâmetro
     */
    @GetMapping("/i18n/locale-test")
    public ResponseEntity<ApiResponse<Map<String, String>>> testLocaleChange() {
        
        Map<String, String> result = new HashMap<>();
        
        // Locale atual
        Locale currentLocale = LocaleContextHolder.getLocale();
        result.put("currentLocale", currentLocale.toString());
        
        // Mensagens no locale atual
        result.put("app.name", messageSourceAccessor.getMessage("app.name", currentLocale));
        result.put("auth.login.success", messageSourceAccessor.getMessage("auth.login.success", currentLocale));
        result.put("user.created", messageSourceAccessor.getMessage("user.created", currentLocale));
        
        String message = MessageUtils.getMessage("test.locale");
        return ResponseEntity.ok(ApiResponse.success(message, result));
    }

    /**
     * Endpoint para testar fallback de mensagens
     */
    @GetMapping("/i18n/fallback")
    public ResponseEntity<ApiResponse<Map<String, String>>> testFallback() {
        
        Map<String, String> result = new HashMap<>();
        Locale currentLocale = LocaleContextHolder.getLocale();
        
        // Chave que existe
        result.put("existing.key", messageSourceAccessor.getMessage("app.name", currentLocale));
        
        // Chave que não existe (MessageSourceAccessor lança exceção, então vamos capturar)
        try {
            result.put("non.existing.key", messageSourceAccessor.getMessage("non.existing.key", currentLocale));
        } catch (Exception e) {
            result.put("non.existing.key", "non.existing.key"); // Fallback manual
        }
        
        // Chave que não existe com fallback
        String fallbackMessage = MessageUtils.getMessage("test.fallback.message");
        result.put("with.fallback", messageSourceAccessor.getMessage("another.non.existing.key", fallbackMessage, currentLocale));
        
        String message = MessageUtils.getMessage("test.fallback");
        return ResponseEntity.ok(ApiResponse.success(message, result));
    }
}