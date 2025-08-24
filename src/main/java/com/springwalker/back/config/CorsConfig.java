package com.springwalker.back.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Define que as configurações de CORS se aplicam a todos os endpoints ("/**").
        registry.addMapping("/**")
                // Permite requisições da origem http://localhost:4200 (onde o front-end pode estar rodando).
                .allowedOrigins("http://localhost:4200")
                // Permite os métodos HTTP especificados (GET, POST, PUT, DELETE, OPTIONS).
                .allowedMethods("GET", "POST", "PUT", "PATCH" , "DELETE", "OPTIONS")
                // Permite todos os cabeçalhos na requisição.
                .allowedHeaders("*")
                // Permite o envio de credenciais (cookies, headers de autenticação, etc.).
                .allowCredentials(true);
    }
}
