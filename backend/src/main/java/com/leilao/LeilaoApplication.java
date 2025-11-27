package com.leilao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Classe principal da aplicação Sistema de Leilão Eletrônico
 * 
 * Configurações habilitadas:
 * - @EnableAsync: Para processamento assíncrono
 * - @EnableScheduling: Para jobs agendados (encerramento de leilões, etc.)
 */
@SpringBootApplication
@EnableAsync
@EnableScheduling
public class LeilaoApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeilaoApplication.class, args);
    }
}