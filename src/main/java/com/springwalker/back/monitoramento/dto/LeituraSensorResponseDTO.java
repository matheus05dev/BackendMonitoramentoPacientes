package com.springwalker.back.monitoramento.dto;

import com.springwalker.back.core.enums.CondicaoSaude;
import com.springwalker.back.core.enums.Gravidade;
import com.springwalker.back.core.enums.TipoDado;
import com.springwalker.back.core.enums.UnidadeMedida;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LeituraSensorResponseDTO {

    private Long id;
    private Long atendimentoId;
    private Double valor;
    private LocalDateTime dataHora;
    private TipoDado tipoDado;
    private UnidadeMedida unidadeMedida;
    private CondicaoSaude condicaoSaude;
    private Gravidade gravidade;

}
