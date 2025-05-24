package com.dany.michelladas.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

/**
 * Configuración para la internacionalización (i18n) de la aplicación Michelladas.
 * Permite cambiar el idioma a través del parámetro `lang` en la URL (?lang=es, ?lang=en, ?lang=ca).
 *
 * Idiomas soportados: Español (por defecto), Inglés, Catalán.
 */
@Configuration
public class InternacionalizacionConfig implements WebMvcConfigurer {

    /**
     * Define el `LocaleResolver` que mantiene el idioma en la sesión del usuario.
     * Por defecto se usará el español.
     *
     * @return el `LocaleResolver` configurado
     */
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(new Locale("es"));
        return slr;
    }

    /**
     * Interceptor que permite cambiar el idioma al detectar el parámetro `lang` en la URL.
     *
     * @return el `LocaleChangeInterceptor` configurado
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");
        return interceptor;
    }

    /**
     * Registra el interceptor de cambio de idioma.
     *
     * @param registry registro de interceptores web
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}

