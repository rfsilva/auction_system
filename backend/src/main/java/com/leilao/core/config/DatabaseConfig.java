package com.leilao.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Configuração do banco de dados e JPA
 * 
 * @EnableJpaAuditing - Habilita auditoria automática (@CreationTimestamp, @UpdateTimestamp)
 * @EnableJpaRepositories - Habilita repositórios JPA
 * @EnableTransactionManagement - Habilita gerenciamento de transações
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.leilao.modules.*.repository")
@EnableJpaAuditing
@EnableTransactionManagement
public class DatabaseConfig {
    
    // A configuração @EnableJpaAuditing garante que:
    // - @CreationTimestamp funcione automaticamente
    // - @UpdateTimestamp funcione automaticamente
    // - Substitui a necessidade de triggers no banco
    
    // Configurações específicas do banco serão adicionadas conforme necessário
}