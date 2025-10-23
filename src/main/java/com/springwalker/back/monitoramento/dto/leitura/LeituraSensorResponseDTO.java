package com.springwalker.back.monitoramento.dto.leitura;

import com.springwalker.back.monitoramento.enums.leitura.CondicaoSaude;
import com.springwalker.back.monitoramento.enums.leitura.Gravidade;
import com.springwalker.back.monitoramento.enums.leitura.TipoDado;
import com.springwalker.back.monitoramento.enums.leitura.UnidadeMedida;
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
