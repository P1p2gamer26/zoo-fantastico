package com.Javeriana.zoo_fantastico.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Configuración global de CORS (Cross-Origin Resource Sharing) para la API REST.
 *
 * <p>Permite que clientes externos (por ejemplo, un frontend en React o Angular
 * corriendo en un puerto diferente) puedan consumir la API sin ser bloqueados
 * por las restricciones de mismo origen del navegador.</p>
 *
 * <p>En producción, reemplaza {@code "*"} en {@code allowedOrigins} por los
 * dominios específicos permitidos.</p>
 */
@Configuration
public class CorsConfig {

    /**
     * Define el filtro CORS aplicado globalmente a todos los endpoints de la API.
     *
     * @return un {@link CorsFilter} configurado con las políticas de acceso.
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // Permite solicitudes desde cualquier origen (ajustar en producción)
        config.addAllowedOriginPattern("*");

        // Permite los métodos HTTP estándar para una API REST
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("OPTIONS");

        // Permite cualquier encabezado en la solicitud
        config.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplica la configuración a todos los endpoints de la API
        source.registerCorsConfiguration("/api/**", config);

        return new CorsFilter(source);
    }
}
