package com.springwalker.back.monitoramento.dto.leitura;

import com.springwalker.back.core.enums.CondicaoSaude;
import com.springwalker.back.core.enums.Gravidade;
import com.springwalker.back.core.enums.TipoDado;
import com.springwalker.back.core.enums.UnidadeMedida;
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
}
