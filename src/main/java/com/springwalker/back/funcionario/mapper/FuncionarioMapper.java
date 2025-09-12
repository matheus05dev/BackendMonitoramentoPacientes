package com.springwalker.back.funcionario.mapper;

import com.springwalker.back.atendimento.model.Atendimento;
import com.springwalker.back.core.mapper.TelefoneMapper;
import com.springwalker.back.funcionario.dto.FuncionarioSaudeRequestDTO;
import com.springwalker.back.funcionario.dto.FuncionarioSaudeResponseDTO;
import com.springwalker.back.funcionario.model.FuncionarioSaude;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {TelefoneMapper.class}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface FuncionarioMapper {

    FuncionarioSaude toEntity(FuncionarioSaudeRequestDTO dto);

    @Mapping(source = "atendimentos", target = "atendimentosIds")
    @Mapping(source = "atendimentosComplicacao", target = "atendimentosComplicacaoIds")
    FuncionarioSaudeResponseDTO toResponseDTO(FuncionarioSaude entity);

    void updateFromDto(FuncionarioSaudeRequestDTO dto, @MappingTarget FuncionarioSaude entity);

    default List<Long> mapAtendimentosToIds(List<Atendimento> atendimentos) {
        if (atendimentos == null || atendimentos.isEmpty()) {
            return List.of();
        }
        return atendimentos.stream()
                .map(Atendimento::getId)
                .collect(Collectors.toList());
    }
}
