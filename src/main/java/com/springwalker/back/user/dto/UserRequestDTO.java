package com.springwalker.back.user.dto;

import com.springwalker.back.user.role.Role;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para criação ou atualização de um usuário")
public record UserRequestDTO(
        @Schema(description = "Nome de usuário", example = "novo.usuario")
        String username,
        @Schema(description = "Senha do usuário", example = "senha123")
        String password,
        @Schema(description = "Papel do usuário", example = "ADMIN", allowableValues = {"ADMIN", "USER"})
        Role role) {
}
