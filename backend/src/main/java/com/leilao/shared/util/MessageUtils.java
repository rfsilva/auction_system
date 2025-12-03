package com.leilao.shared.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Utilitário para facilitar o acesso às mensagens internacionalizadas usando MessageSourceAccessor
 */
@Component
public class MessageUtils {

    private static MessageSourceAccessor messageSourceAccessor;

    @Autowired
    public void setMessageSourceAccessor(MessageSourceAccessor messageSourceAccessor) {
        MessageUtils.messageSourceAccessor = messageSourceAccessor;
    }

    /**
     * Obtém mensagem usando o locale atual do contexto
     */
    public static String getMessage(String key, Object... args) {
        if (messageSourceAccessor == null) {
            return key; // Fallback se não inicializado
        }
        try {
            return messageSourceAccessor.getMessage(key, args, LocaleContextHolder.getLocale());
        } catch (Exception e) {
            return key; // Fallback se chave não encontrada
        }
    }

    /**
     * Obtém mensagem para um locale específico
     */
    public static String getMessage(String key, Locale locale, Object... args) {
        if (messageSourceAccessor == null) {
            return key;
        }
        try {
            return messageSourceAccessor.getMessage(key, args, locale);
        } catch (Exception e) {
            return key;
        }
    }

    /**
     * Obtém mensagem com fallback personalizado
     */
    public static String getMessage(String key, String defaultMessage, Object... args) {
        if (messageSourceAccessor == null) {
            return defaultMessage != null ? defaultMessage : key;
        }
        try {
            return messageSourceAccessor.getMessage(key, args, LocaleContextHolder.getLocale());
        } catch (Exception e) {
            return defaultMessage != null ? defaultMessage : key;
        }
    }

    /**
     * Obtém mensagem para português (idioma padrão)
     */
    public static String getMessagePt(String key, Object... args) {
        return getMessage(key, new Locale("pt", "BR"), args);
    }

    /**
     * Obtém mensagem para inglês
     */
    public static String getMessageEn(String key, Object... args) {
        return getMessage(key, Locale.ENGLISH, args);
    }

    /**
     * Obtém mensagem para espanhol
     */
    public static String getMessageEs(String key, Object... args) {
        return getMessage(key, new Locale("es", "ES"), args);
    }

    /**
     * Obtém mensagem para italiano
     */
    public static String getMessageIt(String key, Object... args) {
        return getMessage(key, new Locale("it", "IT"), args);
    }
}