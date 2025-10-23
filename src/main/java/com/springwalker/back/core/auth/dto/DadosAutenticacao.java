package com.springwalker.back.core.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados de autenticação para login do usuário")
public record DadosAutenticacao(
        @Schema(description = "Login do usuário", example = "usuario.teste")
        String login,
        @Schema(description = "Senha do usuário", example = "123456")
        String senha) {
}
