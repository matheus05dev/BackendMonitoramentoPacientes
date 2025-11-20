package com.springwalker.back.atendimento.mapper;

import com.springwalker.back.atendimento.dto.AtendimentoRequestDTO;
import com.springwalker.back.atendimento.dto.AtendimentoResponseDTO;
import com.springwalker.back.atendimento.model.Atendimento;
import com.springwalker.back.monitoramento.mapper.LeituraSensorMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {LeituraSensorMapper.class})
public interface AtendimentoMapper {

    @Mapping(target = "paciente", ignore = true)
    @Mapping(target = "medicoResponsavel", ignore = true)
    @Mapping(target = "medicoComplicacao", ignore = true)
    @Mapping(target = "statusMonitoramento", source = "statusMonitoramento")
    @Mapping(target = "diagnostico_complicacao", source = "diagnosticoComplicacao")
    @Mapping(target = "tratamento_complicacao", source = "tratamentoComplicacao")
    Atendimento toEntity(AtendimentoRequestDTO dto);

    @Mapping(target = "pacienteId", source = "paciente.id")
    @Mapping(target = "nomePaciente", source = "nomePaciente")
    @Mapping(target = "medicoResponsavelId", source = "medicoResponsavel.id")
    @Mapping(target = "nomeMedicoResponsavel", source = "nomeMedicoResponsavel")
    @Mapping(target = "medicoComplicacaoId", source = "medicoComplicacao.id")
    @Mapping(target = "nomeMedicoComplicacao", source = "nomeMedicoComplicacao")
    @Mapping(target = "numeroQuarto", source = "quarto.numero")
    @Mapping(target = "diagnosticoComplicacao", source = "diagnostico_complicacao")
    @Mapping(target = "tratamentoComplicacao", source = "tratamento_complicacao")
    AtendimentoResponseDTO toResponseDTO(Atendimento atendimento);
}
