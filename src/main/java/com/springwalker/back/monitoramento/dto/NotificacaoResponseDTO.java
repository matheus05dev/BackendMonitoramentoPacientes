package com.springwalker.back.monitoramento.dto;

import com.springwalker.back.core.enums.StatusNotificacao;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificacaoResponseDTO {
    private Long id;
    private LeituraSensorResponseDTO leituraSensor;
    private StatusNotificacao status;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataFechamento;
}
