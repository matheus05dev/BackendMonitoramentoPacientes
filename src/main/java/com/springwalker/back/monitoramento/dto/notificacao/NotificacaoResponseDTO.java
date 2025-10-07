package com.springwalker.back.monitoramento.dto.notificacao;

import com.springwalker.back.core.enums.StatusNotificacao;
import com.springwalker.back.monitoramento.dto.leitura.LeituraSensorResponseDTO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificacaoResponseDTO {
    private Long id;
    private LeituraSensorResponseDTO leituraSensor;
    private StatusNotificacao status;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataFechamento;
    private Integer numeroQuarto;
}
