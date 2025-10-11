package com.springwalker.back.user.dto;

import com.springwalker.back.user.role.Role;
import com.springwalker.back.user.model.User;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados de resposta de um usuário")
public record UserResponseDTO(
        @Schema(description = "ID do usuário", example = "1")
        Long id,
        @Schema(description = "Nome de usuário", example = "novo.usuario")
        String username,
        @Schema(description = "Papel do usuário", example = "ADMIN", allowableValues = {"ADMIN", "USER"})
        Role role) {
    public UserResponseDTO(User user) {
        this(user.getId(), user.getUsername(), user.getRole());
    }
}
