package com.springwalker.back.monitoramento.mapper;

import com.springwalker.back.monitoramento.dto.LeituraSensorRequestDTO;
import com.springwalker.back.monitoramento.dto.LeituraSensorResponseDTO;
import com.springwalker.back.monitoramento.model.LeituraSensor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LeituraSensorMapper {

    LeituraSensorMapper INSTANCE = Mappers.getMapper(LeituraSensorMapper.class);

    LeituraSensor toModel(LeituraSensorRequestDTO dto);

    LeituraSensorResponseDTO toResponse(LeituraSensor model);
}
