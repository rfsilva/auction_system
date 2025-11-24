package com.leilao.core.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * Configuração condicional para email
 * Permite desabilitar email em desenvolvimento
 */
@Configuration
public class EmailConfig {

    /**
     * Configuração real do JavaMailSender quando email está habilitado
     */
    @Bean
    @ConditionalOnProperty(name = "app.email.enabled", havingValue = "true", matchIfMissing = true)
    public JavaMailSender javaMailSender(MailProperties mailProperties) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        
        mailSender.setHost(mailProperties.getHost());
        mailSender.setPort(mailProperties.getPort());
        mailSender.setUsername(mailProperties.getUsername());
        mailSender.setPassword(mailProperties.getPassword());
        
        Properties props = mailSender.getJavaMailProperties();
        props.putAll(mailProperties.getProperties());
        
        return mailSender;
    }

    /**
     * Mock do JavaMailSender quando email está desabilitado
     */
    @Bean
    @ConditionalOnProperty(name = "app.email.enabled", havingValue = "false")
    public JavaMailSender mockJavaMailSender() {
        return new MockJavaMailSender();
    }

    /**
     * Implementação mock do JavaMailSender para desenvolvimento
     */
    public static class MockJavaMailSender extends JavaMailSenderImpl {
        
        @Override
        public void send(org.springframework.mail.SimpleMailMessage simpleMessage) {
            System.out.println("MOCK EMAIL - Enviando email simples:");
            if (simpleMessage.getTo() != null) {
                System.out.println("Para: " + String.join(", ", simpleMessage.getTo()));
            }
            System.out.println("Assunto: " + simpleMessage.getSubject());
            System.out.println("Texto: " + simpleMessage.getText());
        }

        @Override
        public void send(org.springframework.mail.SimpleMailMessage... simpleMessages) {
            for (org.springframework.mail.SimpleMailMessage message : simpleMessages) {
                send(message);
            }
        }

        @Override
        public void send(jakarta.mail.internet.MimeMessage mimeMessage) {
            System.out.println("MOCK EMAIL - Enviando email MIME (HTML)");
        }

        @Override
        public void send(jakarta.mail.internet.MimeMessage... mimeMessages) {
            for (jakarta.mail.internet.MimeMessage message : mimeMessages) {
                send(message);
            }
        }

        /**
         * Mock do teste de conexão - sempre retorna sucesso
         */
        public void testConnection() {
            // Mock - não faz nada, sempre "conecta" com sucesso
            System.out.println("MOCK EMAIL - Teste de conexão simulado (sempre OK)");
        }
    }
}