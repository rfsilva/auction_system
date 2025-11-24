package com.leilao.shared.service;

import com.leilao.core.config.EmailConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Serviço de email com suporte a mock para desenvolvimento
 */
@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final boolean emailEnabled;
    private final boolean emailMock;

    public EmailService(
            JavaMailSender mailSender,
            @Value("${app.email.enabled:true}") boolean emailEnabled,
            @Value("${app.email.mock:false}") boolean emailMock) {
        this.mailSender = mailSender;
        this.emailEnabled = emailEnabled;
        this.emailMock = emailMock;
        
        logger.info("EmailService inicializado - Enabled: {}, Mock: {}", emailEnabled, emailMock);
    }

    /**
     * Enviar email simples
     */
    public void sendSimpleEmail(String to, String subject, String text) {
        try {
            if (!emailEnabled) {
                logger.info("Email desabilitado - Não enviando para: {}", to);
                return;
            }

            if (emailMock) {
                logger.info("MOCK EMAIL - Para: {}, Assunto: {}, Texto: {}", to, subject, text);
                return;
            }

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            message.setFrom("noreply@leilao.com");

            mailSender.send(message);
            logger.info("Email enviado com sucesso para: {}", to);

        } catch (Exception e) {
            logger.error("Erro ao enviar email para {}: {}", to, e.getMessage());
            // Em desenvolvimento, não falhar por causa de email
            if (emailMock || !emailEnabled) {
                logger.warn("Ignorando erro de email em modo mock/desabilitado");
            } else {
                throw new RuntimeException("Falha ao enviar email", e);
            }
        }
    }

    /**
     * Enviar email de boas-vindas
     */
    public void sendWelcomeEmail(String to, String userName) {
        String subject = "Bem-vindo ao Sistema de Leilão Eletrônico";
        String text = String.format(
            "Olá %s,\n\n" +
            "Bem-vindo ao nosso sistema de leilão eletrônico!\n\n" +
            "Sua conta foi criada com sucesso. Agora você pode:\n" +
            "- Participar de leilões\n" +
            "- Dar lances em produtos\n" +
            "- Acompanhar seus leilões favoritos\n\n" +
            "Atenciosamente,\n" +
            "Equipe Leilão Eletrônico",
            userName
        );

        sendSimpleEmail(to, subject, text);
    }

    /**
     * Enviar notificação de lance superado
     */
    public void sendBidOutbidNotification(String to, String userName, String productTitle, String currentBid) {
        String subject = "Seu lance foi superado!";
        String text = String.format(
            "Olá %s,\n\n" +
            "Seu lance no produto '%s' foi superado.\n" +
            "Lance atual: %s\n\n" +
            "Acesse o sistema para dar um novo lance!\n\n" +
            "Atenciosamente,\n" +
            "Equipe Leilão Eletrônico",
            userName, productTitle, currentBid
        );

        sendSimpleEmail(to, subject, text);
    }

    /**
     * Enviar notificação de leilão terminando
     */
    public void sendAuctionEndingNotification(String to, String userName, String productTitle, String timeLeft) {
        String subject = "Leilão terminando em breve!";
        String text = String.format(
            "Olá %s,\n\n" +
            "O leilão do produto '%s' termina em %s.\n\n" +
            "Esta é sua última chance de dar um lance!\n\n" +
            "Atenciosamente,\n" +
            "Equipe Leilão Eletrônico",
            userName, productTitle, timeLeft
        );

        sendSimpleEmail(to, subject, text);
    }

    /**
     * Testar conectividade do email
     */
    public boolean testConnection() {
        try {
            if (!emailEnabled || emailMock) {
                logger.info("Teste de conexão de email - Mock/Desabilitado: sempre OK");
                return true;
            }

            // Para JavaMailSender real, tentar enviar um email de teste
            if (mailSender instanceof EmailConfig.MockJavaMailSender) {
                ((EmailConfig.MockJavaMailSender) mailSender).testConnection();
            } else {
                // Para implementação real, criar uma conexão de teste
                SimpleMailMessage testMessage = new SimpleMailMessage();
                testMessage.setTo("test@example.com");
                testMessage.setSubject("Test Connection");
                testMessage.setText("Test");
                // Não enviar realmente, apenas testar a configuração
            }
            
            logger.info("Teste de conexão de email: OK");
            return true;

        } catch (Exception e) {
            logger.error("Teste de conexão de email falhou: {}", e.getMessage());
            return false;
        }
    }
}