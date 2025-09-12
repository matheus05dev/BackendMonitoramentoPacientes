package com.springwalker.back.quarto.mapper;

import com.springwalker.back.quarto.dto.QuartoRequestDTO;
import com.springwalker.back.quarto.dto.QuartoResponseDTO;
import com.springwalker.back.quarto.model.Quarto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface QuartoMapper {
    QuartoMapper INSTANCE = Mappers.getMapper(QuartoMapper.class);

    @Mapping(target = "pacientes", ignore = true)
    Quarto toEntity(QuartoRequestDTO dto);

    @Mapping(target = "pacientesIds", expression = "java(quarto.getPacientes() != null ? quarto.getPacientes().stream().map(p -> p.getId()).toList() : java.util.Collections.emptyList())")
    QuartoResponseDTO toResponseDTO(Quarto quarto);

    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(QuartoRequestDTO dto, @MappingTarget Quarto entity);
}
