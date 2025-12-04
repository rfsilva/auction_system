package com.leilao.core.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leilao.core.config.RateLimitingConfig;
import com.leilao.shared.dto.ApiResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

/**
 * Filtro de Rate Limiting para proteger endpoints contra abuso
 * História 5: Integração e Otimização - Sprint S2.2
 * 
 * Aplica diferentes limites baseados no tipo de usuário e endpoint.
 * Endpoints de relatórios têm limites mais restritivos.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RateLimitingFilter extends OncePerRequestFilter {

    private final RateLimitingConfig rateLimitingConfig;
    private final MessageSourceAccessor messageSourceAccessor;
    private final ObjectMapper objectMapper;

    // Endpoints que requerem rate limiting mais restritivo
    private static final Set<String> REPORT_ENDPOINTS = Set.of(
            "/contratos/estatisticas",
            "/contratos/comissoes",
            "/contratos/vencendo",
            "/contratos/vencendo/export/csv",
            "/contratos/vencendo/export/pdf",
            "/contratos/projecoes-receita"
    );

    // Endpoints administrativos
    private static final Set<String> ADMIN_ENDPOINTS = Set.of(
            "/admin/usuarios",
            "/admin/dashboard",
            "/contratos/estatisticas",
            "/contratos/comissoes"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        // Pular rate limiting se estiver desabilitado
        if (!rateLimitingConfig.isRateLimitEnabled()) {
            filterChain.doFilter(request, response);
            return;
        }

        // Pular para endpoints que não precisam de rate limiting
        String requestPath = request.getRequestURI();
        if (shouldSkipRateLimit(requestPath)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Determinar tipo de bucket baseado no endpoint e usuário
        RateLimitingConfig.BucketType bucketType = determineBucketType(requestPath, request);
        
        // Obter bucket apropriado
        RateLimitingConfig.RateLimitBucket bucket = getBucketForRequest(request, bucketType);
        
        // Tentar consumir um token
        RateLimitingConfig.ConsumptionResult result = bucket.tryConsume(1);
        
        if (result.isConsumed()) {
            // Adicionar headers informativos
            addRateLimitHeaders(response, result);
            
            // Log para monitoramento
            logRateLimitUsage(request, result, bucketType);
            
            // Continuar com a requisição
            filterChain.doFilter(request, response);
        } else {
            // Rate limit excedido
            handleRateLimitExceeded(request, response, result);
        }
    }

    /**
     * Determina se deve pular rate limiting para este endpoint
     */
    private boolean shouldSkipRateLimit(String requestPath) {
        return requestPath.startsWith("/health") ||
               requestPath.startsWith("/actuator") ||
               requestPath.startsWith("/swagger") ||
               requestPath.startsWith("/v3/api-docs") ||
               requestPath.equals("/auth/login") ||
               requestPath.equals("/auth/register") ||
               requestPath.equals("/auth/refresh");
    }

    /**
     * Determina o tipo de bucket baseado no endpoint e usuário
     */
    private RateLimitingConfig.BucketType determineBucketType(String requestPath, HttpServletRequest request) {
        // Endpoints de relatórios têm limite mais restritivo
        if (REPORT_ENDPOINTS.stream().anyMatch(requestPath::contains)) {
            return RateLimitingConfig.BucketType.REPORT;
        }
        
        // Verificar se é admin
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"))) {
            return RateLimitingConfig.BucketType.ADMIN;
        }
        
        return RateLimitingConfig.BucketType.DEFAULT;
    }

    /**
     * Obtém bucket apropriado para a requisição
     */
    private RateLimitingConfig.RateLimitBucket getBucketForRequest(HttpServletRequest request, RateLimitingConfig.BucketType bucketType) {
        // Tentar usar ID do usuário se autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            return rateLimitingConfig.getBucketForUser(auth.getName(), bucketType);
        }
        
        // Usar IP como fallback
        String clientIp = getClientIpAddress(request);
        return rateLimitingConfig.getBucketForIp(clientIp, bucketType);
    }

    /**
     * Adiciona headers informativos sobre rate limiting
     */
    private void addRateLimitHeaders(HttpServletResponse response, RateLimitingConfig.ConsumptionResult result) {
        response.addHeader("X-RateLimit-Remaining", String.valueOf(result.getRemainingTokens()));
        
        if (result.getSecondsToWait() > 0) {
            response.addHeader("X-RateLimit-Retry-After", String.valueOf(result.getSecondsToWait()));
        }
    }

    /**
     * Trata quando rate limit é excedido
     */
    private void handleRateLimitExceeded(HttpServletRequest request, HttpServletResponse response, 
                                       RateLimitingConfig.ConsumptionResult result) throws IOException {
        
        String clientIp = getClientIpAddress(request);
        String userAgent = request.getHeader("User-Agent");
        
        log.warn("Rate limit excedido - IP: {}, User-Agent: {}, Path: {}", 
                clientIp, userAgent, request.getRequestURI());
        
        // Calcular tempo de espera
        long secondsToWait = result.getSecondsToWait();
        
        // Configurar resposta
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.addHeader("X-RateLimit-Retry-After", String.valueOf(secondsToWait));
        
        // Mensagem localizada
        String errorMessage = messageSourceAccessor.getMessage(
                "rate.limit.exceeded", 
                new Object[]{secondsToWait}, 
                LocaleContextHolder.getLocale()
        );
        
        ApiResponse<Object> errorResponse = ApiResponse.error(errorMessage);
        
        // Escrever resposta JSON
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }

    /**
     * Log para monitoramento de uso do rate limiting
     */
    private void logRateLimitUsage(HttpServletRequest request, RateLimitingConfig.ConsumptionResult result, 
                                 RateLimitingConfig.BucketType bucketType) {
        
        // Log apenas quando tokens estão baixos (< 10% do limite)
        if (result.getRemainingTokens() < 6) { // Assumindo limite padrão de 60/min
            String clientIp = getClientIpAddress(request);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String userId = auth != null ? auth.getName() : "anonymous";
            
            log.info("Rate limit usage - User: {}, IP: {}, Type: {}, Remaining: {}, Path: {}", 
                    userId, clientIp, bucketType, result.getRemainingTokens(), request.getRequestURI());
        }
    }

    /**
     * Obtém endereço IP real do cliente considerando proxies
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
}