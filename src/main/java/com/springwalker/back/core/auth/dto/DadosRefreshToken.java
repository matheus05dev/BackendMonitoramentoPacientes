package com.springwalker.back.core.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record DadosRefreshToken(
        @NotBlank
        String refreshToken
) {
}
