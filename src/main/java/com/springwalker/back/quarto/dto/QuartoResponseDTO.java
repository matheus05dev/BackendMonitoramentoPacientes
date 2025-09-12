package com.springwalker.back.quarto.dto;

import com.springwalker.back.core.enums.LocalizacaoQuarto;
import com.springwalker.back.core.enums.TipoQuarto;
import lombok.Data;
import java.util.List;

@Data
public class QuartoResponseDTO {
    private Long id;
    private Integer numero;
    private LocalizacaoQuarto localizacao;
    private TipoQuarto tipo;
    private Integer capacidade;
    private List<Long> pacientesIds;
}

