package com.springwalker.back.monitoramento.dto;

import com.springwalker.back.core.enums.TipoDado;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LeituraSensorResponseDTO {

    private Long id;
    private Double valor;
    private LocalDateTime dataHora;
    private TipoDado tipoDado;
    private String unidadeMedida;

}
