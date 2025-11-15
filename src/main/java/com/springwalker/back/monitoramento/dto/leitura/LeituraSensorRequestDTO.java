package com.springwalker.back.monitoramento.dto.leitura;

import com.springwalker.back.monitoramento.enums.leitura.CondicaoSaude;
import com.springwalker.back.monitoramento.enums.leitura.Gravidade;
import com.springwalker.back.monitoramento.enums.leitura.TipoDado;
import com.springwalker.back.monitoramento.enums.leitura.UnidadeMedida;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LeituraSensorRequestDTO {

    @NotNull
    private Double valor;

    @NotNull
    private TipoDado tipoDado;

    @NotNull
    private UnidadeMedida unidadeMedida;

    private CondicaoSaude condicaoSaude;

    private Gravidade gravidade;

    private Integer duracaoEstimadaMinutos;
}
