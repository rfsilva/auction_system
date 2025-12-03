package com.leilao.core.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Arrays;
import java.util.Locale;

/**
 * Configuração de internacionalização (i18n) com MessageSourceAccessor
 */
@Configuration
public class LocaleConfig implements WebMvcConfigurer {

    /**
     * Configuração do MessageSource para carregar mensagens dos arquivos de propriedades
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages/messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setDefaultLocale(new Locale("pt", "BR")); // Português como padrão
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
     * Resolver de locale baseado no header Accept-Language
     */
    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
        localeResolver.setDefaultLocale(new Locale("pt", "BR")); // Português como padrão
        localeResolver.setSupportedLocales(Arrays.asList(
            new Locale("pt", "BR"), // Português (Brasil)
            new Locale("en", "US"), // Inglês (Estados Unidos)
            new Locale("es", "ES"), // Espanhol (Espanha)
            new Locale("it", "IT")  // Italiano (Itália)
        ));
        return localeResolver;
    }

    /**
     * Interceptor para mudança de locale via parâmetro
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang"); // Parâmetro para mudança de idioma: ?lang=en
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