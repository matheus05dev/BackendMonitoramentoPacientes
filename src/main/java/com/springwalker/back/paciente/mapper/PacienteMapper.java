package com.springwalker.back.paciente.mapper;

import com.springwalker.back.core.mapper.TelefoneMapper;
import com.springwalker.back.paciente.dto.PacienteRequestDTO;
import com.springwalker.back.paciente.dto.PacienteResponseDTO;
import com.springwalker.back.paciente.model.Paciente;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {TelefoneMapper.class}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PacienteMapper {

    @Mapping(target = "quarto", ignore = true) // O quarto será tratado no serviço
    Paciente toEntity(PacienteRequestDTO dto);

    @Mapping(source = "quarto.id", target = "quartoId")
    PacienteResponseDTO toResponseDTO(Paciente entity);

    @Mapping(target = "quarto", ignore = true) // O quarto será tratado no serviço
    void updateFromDto(PacienteRequestDTO dto, @MappingTarget Paciente entity);

}
