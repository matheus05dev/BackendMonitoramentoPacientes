package com.springwalker.back.atendimento.dto;

import com.springwalker.back.core.enums.Diagnostico;
import com.springwalker.back.core.enums.StatusMonitoramento;
import com.springwalker.back.core.enums.StatusPaciente;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AtendimentoRequestDTO {
    private Long pacienteId;
    private Long medicoResponsavelId;
    private Long medicoComplicacaoId;
    private StatusPaciente statusPaciente;
    private String acompanhante;
    private String condicoesPreexistentes;
    private Diagnostico diagnostico;
    private String tratamento;
    private LocalDateTime dataEntrada;
    private LocalDateTime dataSaida;
    private String observacoes;
    private Diagnostico diagnosticoComplicacao;
    private String tratamentoComplicacao;
    private StatusMonitoramento statusMonitoramento;
}
