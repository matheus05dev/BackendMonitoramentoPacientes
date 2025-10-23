package com.springwalker.back.core.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Monitoramento de Pacientes")
                        .version("1.0")
                        .description(
                                "API para gerenciamento de pacientes, atendimentos, funcionários e quartos em um sistema de monitoramento hospitalar")
                        .contact(new Contact()
                                .name("Springwalker")
                                .email("support@springwalker.com")));
    }
}
