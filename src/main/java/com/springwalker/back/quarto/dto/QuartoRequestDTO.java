package com.springwalker.back.quarto.dto;

import com.springwalker.back.core.enums.LocalizacaoQuarto;
import com.springwalker.back.core.enums.TipoQuarto;
import lombok.Data;

@Data
public class QuartoRequestDTO {
    private Integer numero;
    private LocalizacaoQuarto localizacao;
    private TipoQuarto tipo;
    private Integer capacidade;
}

