package com.springwalker.back.quarto.dto;

import com.springwalker.back.quarto.enums.LocalizacaoQuarto;
import com.springwalker.back.quarto.enums.TipoQuarto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class QuartoRequestDTO {

    @NotNull
    private Integer numero;

    @NotNull
    private LocalizacaoQuarto localizacao;

    @NotNull
    private TipoQuarto tipo;

    @NotNull
    private Integer capacidade;
}

