package com.springwalker.back.monitoramento.mapper;

import com.springwalker.back.monitoramento.dto.leitura.LeituraSensorRequestDTO;
import com.springwalker.back.monitoramento.dto.leitura.LeituraSensorResponseDTO;
import com.springwalker.back.monitoramento.model.LeituraSensor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LeituraSensorMapper {

    LeituraSensorMapper INSTANCE = Mappers.getMapper(LeituraSensorMapper.class);

    @Mapping(source = "duracaoEstimadaMinutos", target = "duracaoEstimadaMinutos")
    @Mapping(source = "dataHora", target = "dataHora")
    @Mapping(source = "codigo", target = "codigo")
    LeituraSensor toModel(LeituraSensorRequestDTO dto);

    @Mapping(source = "atendimento.id", target = "atendimentoId")
    @Mapping(source = "codigo", target = "codigo")
    LeituraSensorResponseDTO toResponse(LeituraSensor model);
}
