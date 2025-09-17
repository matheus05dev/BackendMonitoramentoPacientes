package com.springwalker.back.atendimento.mapper;

import com.springwalker.back.atendimento.dto.AtendimentoRequestDTO;
import com.springwalker.back.atendimento.dto.AtendimentoResponseDTO;
import com.springwalker.back.atendimento.model.Atendimento;
import com.springwalker.back.monitoramento.mapper.LeituraSensorMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {LeituraSensorMapper.class})
public interface AtendimentoMapper {
    AtendimentoMapper INSTANCE = Mappers.getMapper(AtendimentoMapper.class);

    @Mapping(target = "paciente", ignore = true)
    @Mapping(target = "medicoResponsavel", ignore = true)
    @Mapping(target = "medicoComplicacao", ignore = true)
    @Mapping(target = "statusMonitoramento", source = "statusMonitoramento")
    Atendimento toEntity(AtendimentoRequestDTO dto);

    @Mapping(target = "pacienteId", source = "paciente.id")
    @Mapping(target = "nomePaciente", source = "paciente.nome", defaultValue = "")
    @Mapping(target = "medicoResponsavelId", source = "medicoResponsavel.id")
    @Mapping(target = "nomeMedicoResponsavel", source = "medicoResponsavel.nome", defaultValue = "")
    @Mapping(target = "medicoComplicacaoId", source = "medicoComplicacao.id")
    @Mapping(target = "nomeMedicoComplicacao", source = "medicoComplicacao.nome", defaultValue = "null")
    AtendimentoResponseDTO toResponseDTO(Atendimento atendimento);
}
