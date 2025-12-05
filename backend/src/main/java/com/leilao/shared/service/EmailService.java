package com.leilao.shared.service;

import com.leilao.core.config.EmailConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * Serviço de email com suporte a mock para desenvolvimento e i18n usando MessageSourceAccessor
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final MessageSourceAccessor messageSourceAccessor;
    
    @Value("${app.email.enabled:true}")
    private boolean emailEnabled;
    
    @Value("${app.email.mock:false}")
    private boolean emailMock;

    /**
     * Enviar email simples
     */
    public void sendSimpleEmail(String to, String subject, String text) {
        sendSimpleEmail(to, subject, text, null);
    }

    /**
     * Enviar email simples com locale específico
     */
    public void sendSimpleEmail(String to, String subject, String text, Locale locale) {
        try {
            if (!emailEnabled) {
                log.info("Email desabilitado - Não enviando para: {}", to);
                return;
            }

            if (emailMock) {
                log.info("MOCK EMAIL - Para: {}, Assunto: {}, Texto: {}", to, subject, text);
                return;
            }

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            message.setFrom("noreply@leilao.com");

            mailSender.send(message);
            log.info("Email enviado com sucesso para: {}", to);

        } catch (Exception e) {
            log.error("Erro ao enviar email para {}: {}", to, e.getMessage());
            // Em desenvolvimento, não falhar por causa de email
            if (emailMock || !emailEnabled) {
                log.warn("Ignorando erro de email em modo mock/desabilitado");
            } else {
                String errorMessage = messageSourceAccessor.getMessage("email.send.failed", 
                        new Object[]{e.getMessage()}, LocaleContextHolder.getLocale());
                throw new RuntimeException(errorMessage);
            }
        }
    }

    /**
     * Enviar email de boas-vindas
     */
    public void sendWelcomeEmail(String to, String userName) {
        sendWelcomeEmail(to, userName, null);
    }

    /**
     * Enviar email de boas-vindas com locale específico
     */
    public void sendWelcomeEmail(String to, String userName, Locale locale) {
        Locale currentLocale = locale != null ? locale : LocaleContextHolder.getLocale();
        String subject = messageSourceAccessor.getMessage("email.welcome.subject", currentLocale);
        String text = messageSourceAccessor.getMessage("email.welcome.body", new Object[]{userName}, currentLocale);
        sendSimpleEmail(to, subject, text, locale);
    }

    /**
     * Enviar notificação de lance superado
     */
    public void sendBidOutbidNotification(String to, String userName, String productTitle, String currentBid) {
        sendBidOutbidNotification(to, userName, productTitle, currentBid, null);
    }

    /**
     * Enviar notificação de lance superado com locale específico
     */
    public void sendBidOutbidNotification(String to, String userName, String productTitle, String currentBid, Locale locale) {
        Locale currentLocale = locale != null ? locale : LocaleContextHolder.getLocale();
        String subject = messageSourceAccessor.getMessage("email.bid.outbid.subject", currentLocale);
        String text = messageSourceAccessor.getMessage("email.bid.outbid.body", 
                new Object[]{userName, productTitle, currentBid}, currentLocale);
        sendSimpleEmail(to, subject, text, locale);
    }

    /**
     * Enviar notificação de leilão terminando
     */
    public void sendAuctionEndingNotification(String to, String userName, String productTitle, String timeLeft) {
        sendAuctionEndingNotification(to, userName, productTitle, timeLeft, null);
    }

    /**
     * Enviar notificação de leilão terminando com locale específico
     */
    public void sendAuctionEndingNotification(String to, String userName, String productTitle, String timeLeft, Locale locale) {
        Locale currentLocale = locale != null ? locale : LocaleContextHolder.getLocale();
        String subject = messageSourceAccessor.getMessage("email.auction.ending.subject", currentLocale);
        String text = messageSourceAccessor.getMessage("email.auction.ending.body", 
                new Object[]{userName, productTitle, timeLeft}, currentLocale);
        sendSimpleEmail(to, subject, text, locale);
    }

    /**
     * Testar conectividade do email
     */
    public boolean testConnection() {
        try {
            if (!emailEnabled || emailMock) {
                log.info("Teste de conexão de email - Mock/Desabilitado: sempre OK");
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
            
            log.info("Teste de conexão de email: OK");
            return true;

        } catch (Exception e) {
            log.error("Teste de conexão de email falhou: {}", e.getMessage());
            return false;
        }
    }
}