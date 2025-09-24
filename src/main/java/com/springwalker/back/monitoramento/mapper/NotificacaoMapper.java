package com.springwalker.back.monitoramento.mapper;

import com.springwalker.back.monitoramento.dto.notificacao.NotificacaoResponseDTO;
import com.springwalker.back.monitoramento.model.Notificacao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {LeituraSensorMapper.class})
public interface NotificacaoMapper {

    NotificacaoMapper INSTANCE = Mappers.getMapper(NotificacaoMapper.class);

    @Mapping(source = "leituraSensor", target = "leituraSensor")
    NotificacaoResponseDTO toResponse(Notificacao notificacao);

    // Se necessário, um método para converter DTO para Model
    // Notificacao toModel(NotificacaoRequestDTO dto);
}
