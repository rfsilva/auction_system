package com.leilao.core.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

/**
 * Configuração de compressão de resposta HTTP
 * História 5: Integração e Otimização - Sprint S2.2
 * 
 * Configura compressão GZIP para reduzir tamanho das respostas,
 * especialmente importante para relatórios grandes.
 */
@Slf4j
@Configuration
public class CompressionConfig {

    /**
     * Configura compressão GZIP no Tomcat embarcado
     */
    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> compressionCustomizer() {
        return factory -> {
            factory.addConnectorCustomizers(connector -> {
                // Habilitar compressão
                connector.setProperty("compression", "on");
                
                // Tipos MIME para compressão
                connector.setProperty("compressableMimeType", 
                    String.join(",",
                        MediaType.APPLICATION_JSON_VALUE,
                        MediaType.APPLICATION_XML_VALUE,
                        MediaType.TEXT_HTML_VALUE,
                        MediaType.TEXT_XML_VALUE,
                        MediaType.TEXT_PLAIN_VALUE,
                        "text/css", // Usando string literal em vez de constante inexistente
                        "application/javascript",
                        "text/javascript",
                        "application/csv",
                        "text/csv"
                    )
                );
                
                // Tamanho mínimo para compressão (2KB)
                connector.setProperty("compressionMinSize", "2048");
                
                // User agents que não suportam compressão (legado)
                connector.setProperty("noCompressionUserAgents", "gozilla, traviata");
                
                log.info("Compressão GZIP configurada para Tomcat");
            });
        };
    }
}