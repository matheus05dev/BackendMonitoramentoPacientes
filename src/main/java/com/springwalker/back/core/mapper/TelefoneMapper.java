package com.springwalker.back.core.mapper;

import com.springwalker.back.core.dto.TelefoneDTO;
import com.springwalker.back.core.model.Telefone;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TelefoneMapper {

    TelefoneMapper INSTANCE = Mappers.getMapper(TelefoneMapper.class);

    TelefoneDTO toDTO(Telefone telefone);

    Telefone toEntity(TelefoneDTO telefoneDTO);

    List<TelefoneDTO> toDTO(List<Telefone> telefones);

    List<Telefone> toEntity(List<TelefoneDTO> telefoneDTOS);
}
