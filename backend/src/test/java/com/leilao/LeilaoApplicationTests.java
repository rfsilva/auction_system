package com.leilao;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Teste básico de inicialização da aplicação
 */
@SpringBootTest
@ActiveProfiles("test")
class LeilaoApplicationTests {

    @Test
    void contextLoads() {
        // Teste básico para verificar se o contexto Spring carrega corretamente
    }
}