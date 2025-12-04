package com.springwalker.back.monitoramento.mapper;

import com.springwalker.back.atendimento.model.Atendimento;
import com.springwalker.back.monitoramento.dto.leitura.LeituraSensorRequestDTO;
import com.springwalker.back.monitoramento.dto.leitura.LeituraSensorResponseDTO;
import com.springwalker.back.monitoramento.enums.leitura.CondicaoSaude;
import com.springwalker.back.monitoramento.enums.leitura.Gravidade;
import com.springwalker.back.monitoramento.enums.leitura.TipoDado;
import com.springwalker.back.monitoramento.enums.leitura.UnidadeMedida;
import com.springwalker.back.monitoramento.model.LeituraSensor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("Testes para LeituraSensorMapper")
class LeituraSensorMapperTest {

    private LeituraSensorMapper mapper = Mappers.getMapper(LeituraSensorMapper.class);

    @Test
    @DisplayName("Deve mapear LeituraSensorRequestDTO para LeituraSensor model corretamente")
    void toModel_shouldMapRequestDTOToModel() {
        // Given
        LeituraSensorRequestDTO requestDTO = new LeituraSensorRequestDTO();
        requestDTO.setValor(37.0);
        requestDTO.setDataHora(LocalDateTime.of(2023, 1, 20, 14, 0));
        requestDTO.setTipoDado(TipoDado.TEMPERATURA);
        requestDTO.setUnidadeMedida(UnidadeMedida.CELSIUS);

        // When
        LeituraSensor model = mapper.toModel(requestDTO);

        // Then
        assertNotNull(model);
        assertEquals(requestDTO.getValor(), model.getValor());
        assertEquals(requestDTO.getDataHora(), model.getDataHora());
        assertEquals(requestDTO.getTipoDado(), model.getTipoDado());
        assertEquals(requestDTO.getUnidadeMedida(), model.getUnidadeMedida());
        assertNull(model.getId());
        assertNull(model.getAtendimento());
        assertNull(model.getGravidade());
        assertNull(model.getCondicaoSaude());
    }

    @Test
    @DisplayName("Deve mapear LeituraSensor model para LeituraSensorResponseDTO corretamente")
    void toResponse_shouldMapModelToResponseDTO() {
        // Given
        Atendimento atendimento = new Atendimento();
        atendimento.setId(5L);
        atendimento.setNumeroQuarto(202);

        LeituraSensor model = LeituraSensor.builder()
                .id(10L)
                .valor(95.0)
                .dataHora(LocalDateTime.of(2023, 1, 20, 14, 30))
                .tipoDado(TipoDado.TEMPERATURA)
                .unidadeMedida(UnidadeMedida.CELSIUS)
                .atendimento(atendimento)
                .gravidade(Gravidade.NORMAL)
                .condicaoSaude(CondicaoSaude.NORMAL)
                .build();

        // When
        LeituraSensorResponseDTO responseDTO = mapper.toResponse(model);

        // Then
        assertNotNull(responseDTO);
        assertEquals(model.getId(), responseDTO.getId());
        assertEquals(model.getValor(), responseDTO.getValor());
        assertEquals(model.getDataHora(), responseDTO.getDataHora());
        assertEquals(model.getTipoDado(), responseDTO.getTipoDado());
        assertEquals(model.getUnidadeMedida(), responseDTO.getUnidadeMedida());
        assertEquals(model.getGravidade(), responseDTO.getGravidade());
        assertEquals(model.getCondicaoSaude(), responseDTO.getCondicaoSaude());
        assertEquals(atendimento.getId(), responseDTO.getAtendimentoId());
    }

    @Test
    @DisplayName("Deve lidar com LeituraSensorRequestDTO nulo")
    void toModel_shouldHandleNullRequestDTO() {
        // When
        LeituraSensor model = mapper.toModel(null);

        // Then
        assertNull(model);
    }

    @Test
    @DisplayName("Deve lidar com LeituraSensor model nulo")
    void toResponse_shouldHandleNullModel() {
        // When
        LeituraSensorResponseDTO responseDTO = mapper.toResponse(null);

        // Then
        assertNull(responseDTO);
    }
}
