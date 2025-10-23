package com.springwalker.back.pessoa.mapper;

import com.springwalker.back.pessoa.dto.TelefoneDTO;
import com.springwalker.back.pessoa.model.Telefone;
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
