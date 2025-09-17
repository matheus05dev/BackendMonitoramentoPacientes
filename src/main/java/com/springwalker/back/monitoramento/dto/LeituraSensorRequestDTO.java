package com.springwalker.back.monitoramento.dto;

import com.springwalker.back.core.enums.TipoDado;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LeituraSensorRequestDTO {

    @NotNull
    private Double valor;

    @NotNull
    private TipoDado tipoDado;

    @NotNull
    private String unidadeMedida;

}
