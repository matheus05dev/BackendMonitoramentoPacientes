package com.springwalker.back.core.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados do token JWT gerado após autenticação")
public record DadosTokenJWT(
        @Schema(description = "Token JWT para autenticação", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String token) {
}
