package com.springwalker.back.core.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados do token JWT gerado após autenticação")
public record DadosTokenJWT(
        @Schema(description = "Token de acesso (Access Token) para autenticação nas requisições", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String accessToken,
        @Schema(description = "Token de atualização (Refresh Token) para obter um novo access token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String refreshToken
) {
}
