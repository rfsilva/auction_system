package com.leilao.core.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Configuração de Rate Limiting usando implementação simples
 * História 5: Integração e Otimização - Sprint S2.2
 * 
 * Implementa rate limiting usando estruturas Java nativas
 * para evitar dependências externas complexas.
 */
@Slf4j
@Configuration
public class RateLimitingConfig {

    @Value("${app.rate-limit.enabled:true}")
    private boolean rateLimitEnabled;

    @Value("${app.rate-limit.requests-per-minute:60}")
    private int requestsPerMinute;

    @Value("${app.rate-limit.admin-requests-per-minute:120}")
    private int adminRequestsPerMinute;

    @Value("${app.rate-limit.report-requests-per-minute:10}")
    private int reportRequestsPerMinute;

    // Cache local de buckets para performance
    private final ConcurrentMap<String, RateLimitBucket> bucketCache = new ConcurrentHashMap<>();

    /**
     * Cria bucket para rate limiting geral
     */
    @Bean
    public RateLimitBucket defaultRateLimitBucket() {
        return new RateLimitBucket(requestsPerMinute, Duration.ofMinutes(1));
    }

    /**
     * Obtém bucket para um usuário específico
     */
    public RateLimitBucket getBucketForUser(String userId, BucketType type) {
        if (!rateLimitEnabled) {
            // Se rate limiting está desabilitado, retorna bucket sem limite
            return new RateLimitBucket(Integer.MAX_VALUE, Duration.ofMinutes(1));
        }

        String key = type.name() + ":" + userId;
        return bucketCache.computeIfAbsent(key, k -> createBucketByType(type));
    }

    /**
     * Obtém bucket para um IP específico
     */
    public RateLimitBucket getBucketForIp(String ipAddress, BucketType type) {
        if (!rateLimitEnabled) {
            return new RateLimitBucket(Integer.MAX_VALUE, Duration.ofMinutes(1));
        }

        String key = type.name() + ":IP:" + ipAddress;
        return bucketCache.computeIfAbsent(key, k -> createBucketByType(type));
    }

    /**
     * Cria bucket baseado no tipo
     */
    private RateLimitBucket createBucketByType(BucketType type) {
        return switch (type) {
            case ADMIN -> new RateLimitBucket(adminRequestsPerMinute, Duration.ofMinutes(1));
            case REPORT -> new RateLimitBucket(reportRequestsPerMinute, Duration.ofMinutes(1));
            case DEFAULT -> new RateLimitBucket(requestsPerMinute, Duration.ofMinutes(1));
        };
    }

    /**
     * Verifica se rate limiting está habilitado
     */
    public boolean isRateLimitEnabled() {
        return rateLimitEnabled;
    }

    /**
     * Limpa cache de buckets (útil para testes)
     */
    public void clearBucketCache() {
        bucketCache.clear();
        log.info("Cache de buckets de rate limiting limpo");
    }

    /**
     * Obtém estatísticas do cache de buckets
     */
    public RateLimitStats getStats() {
        return RateLimitStats.builder()
                .enabled(rateLimitEnabled)
                .bucketsInCache(bucketCache.size())
                .defaultLimit(requestsPerMinute)
                .adminLimit(adminRequestsPerMinute)
                .reportLimit(reportRequestsPerMinute)
                .build();
    }

    /**
     * Tipos de bucket para diferentes cenários
     */
    public enum BucketType {
        DEFAULT,    // Usuários normais
        ADMIN,      // Administradores
        REPORT      // Endpoints de relatórios
    }

    /**
     * Implementação simples de bucket de rate limiting
     */
    public static class RateLimitBucket {
        private final int maxTokens;
        private final Duration refillPeriod;
        private int availableTokens;
        private LocalDateTime lastRefill;

        public RateLimitBucket(int maxTokens, Duration refillPeriod) {
            this.maxTokens = maxTokens;
            this.refillPeriod = refillPeriod;
            this.availableTokens = maxTokens;
            this.lastRefill = LocalDateTime.now();
        }

        public synchronized ConsumptionResult tryConsume(int tokens) {
            refillIfNeeded();
            
            if (availableTokens >= tokens) {
                availableTokens -= tokens;
                return new ConsumptionResult(true, availableTokens, 0);
            } else {
                long secondsToWait = Duration.between(LocalDateTime.now(), 
                    lastRefill.plus(refillPeriod)).getSeconds();
                return new ConsumptionResult(false, availableTokens, Math.max(0, secondsToWait));
            }
        }

        private void refillIfNeeded() {
            LocalDateTime now = LocalDateTime.now();
            if (Duration.between(lastRefill, now).compareTo(refillPeriod) >= 0) {
                availableTokens = maxTokens;
                lastRefill = now;
            }
        }

        public int getAvailableTokens() {
            refillIfNeeded();
            return availableTokens;
        }
    }

    /**
     * Resultado do consumo de tokens
     */
    public static class ConsumptionResult {
        private final boolean consumed;
        private final int remainingTokens;
        private final long secondsToWait;

        public ConsumptionResult(boolean consumed, int remainingTokens, long secondsToWait) {
            this.consumed = consumed;
            this.remainingTokens = remainingTokens;
            this.secondsToWait = secondsToWait;
        }

        public boolean isConsumed() { return consumed; }
        public int getRemainingTokens() { return remainingTokens; }
        public long getSecondsToWait() { return secondsToWait; }
    }

    /**
     * Estatísticas do rate limiting
     */
    public static class RateLimitStats {
        private final boolean enabled;
        private final int bucketsInCache;
        private final int defaultLimit;
        private final int adminLimit;
        private final int reportLimit;

        private RateLimitStats(boolean enabled, int bucketsInCache, int defaultLimit, int adminLimit, int reportLimit) {
            this.enabled = enabled;
            this.bucketsInCache = bucketsInCache;
            this.defaultLimit = defaultLimit;
            this.adminLimit = adminLimit;
            this.reportLimit = reportLimit;
        }

        public static Builder builder() {
            return new Builder();
        }

        // Getters
        public boolean isEnabled() { return enabled; }
        public int getBucketsInCache() { return bucketsInCache; }
        public int getDefaultLimit() { return defaultLimit; }
        public int getAdminLimit() { return adminLimit; }
        public int getReportLimit() { return reportLimit; }

        public static class Builder {
            private boolean enabled;
            private int bucketsInCache;
            private int defaultLimit;
            private int adminLimit;
            private int reportLimit;

            public Builder enabled(boolean enabled) { this.enabled = enabled; return this; }
            public Builder bucketsInCache(int bucketsInCache) { this.bucketsInCache = bucketsInCache; return this; }
            public Builder defaultLimit(int defaultLimit) { this.defaultLimit = defaultLimit; return this; }
            public Builder adminLimit(int adminLimit) { this.adminLimit = adminLimit; return this; }
            public Builder reportLimit(int reportLimit) { this.reportLimit = reportLimit; return this; }

            public RateLimitStats build() {
                return new RateLimitStats(enabled, bucketsInCache, defaultLimit, adminLimit, reportLimit);
            }
        }
    }
}