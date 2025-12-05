package com.leilao.core.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

/**
 * Configuração de internacionalização (i18n) com MessageSourceAccessor
 * História 5: Integração e Otimização - Sprint S2.2
 */
@Configuration
public class LocaleConfig implements WebMvcConfigurer {

    /**
     * Configuração do MessageSource para carregar mensagens dos arquivos .properties
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages/messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setDefaultLocale(new Locale("pt", "BR"));
        messageSource.setCacheSeconds(3600); // Cache por 1 hora
        messageSource.setFallbackToSystemLocale(false);
        return messageSource;
    }

    /**
     * MessageSourceAccessor para acesso simplificado às mensagens
     * Usa o locale atual do contexto automaticamente
     */
    @Bean
    public MessageSourceAccessor messageSourceAccessor(MessageSource messageSource) {
        return new MessageSourceAccessor(messageSource, new Locale("pt", "BR"));
    }

    /**
     * Resolver de locale baseado na sessão
     */
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(new Locale("pt", "BR"));
        return localeResolver;
    }

    /**
     * Interceptor para mudança de locale via parâmetro
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");
        return interceptor;
    }

    /**
     * Registra o interceptor de mudança de locale
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}