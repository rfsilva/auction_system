package com.leilao.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Configuração do banco de dados e JPA
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.leilao.modules.*.repository")
@EnableJpaAuditing
@EnableTransactionManagement
public class DatabaseConfig {
    // Configurações específicas do banco serão adicionadas conforme necessário
}