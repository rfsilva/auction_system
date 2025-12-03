package com.leilao.core.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuração de cache Redis
 * História 5: Integração e Otimização - Sprint S2.2
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Configuração do cache manager com TTL específico por cache
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10)) // TTL padrão de 10 minutos
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer()));

        // Configurações específicas por cache
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        
        // Cache de estatísticas de contratos - 5 minutos conforme especificação
        cacheConfigurations.put("contrato-estatisticas", 
                defaultConfig.entryTtl(Duration.ofMinutes(5)));
        
        // Cache de relatórios de comissões - 15 minutos (dados menos voláteis)
        cacheConfigurations.put("comissao-relatorios", 
                defaultConfig.entryTtl(Duration.ofMinutes(15)));
        
        // Cache de categorias - 1 hora (dados estáticos)
        cacheConfigurations.put("contrato-categorias", 
                defaultConfig.entryTtl(Duration.ofHours(1)));
        
        // Cache de vendedores ativos - 10 minutos
        cacheConfigurations.put("vendedores-ativos", 
                defaultConfig.entryTtl(Duration.ofMinutes(10)));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }
}