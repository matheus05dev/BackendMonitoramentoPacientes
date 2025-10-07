package com.springwalker.back.atendimento.dto;

import com.springwalker.back.core.enums.Diagnostico;
import com.springwalker.back.core.enums.StatusMonitoramento;
import com.springwalker.back.core.enums.StatusPaciente;
import com.springwalker.back.monitoramento.dto.leitura.LeituraSensorResponseDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


@Data
public class AtendimentoResponseDTO {
    private Long id;
    private Long pacienteId;
    private Long medicoResponsavelId;
    private Long medicoComplicacaoId;
    private StatusPaciente statusPaciente;
    private StatusMonitoramento statusMonitoramento;
    private String acompanhante;
    private String condicoesPreexistentes;
    private Diagnostico diagnostico;
    private String tratamento;
    private Integer numeroQuarto;
    private LocalDateTime dataEntrada;
    private LocalDateTime dataSaida;
    private String observacoes;
    private Diagnostico diagnosticoComplicacao;
    private String tratamentoComplicacao;
    private String nomePaciente;
    private String nomeMedicoResponsavel;
    private String nomeMedicoComplicacao;
    private List<LeituraSensorResponseDTO> leituras;
}
