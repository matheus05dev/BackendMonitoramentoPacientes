package com.springwalker.back.monitoramento.mapper;

import com.springwalker.back.atendimento.model.Atendimento;
import com.springwalker.back.monitoramento.dto.notificacao.NotificacaoResponseDTO;
import com.springwalker.back.monitoramento.enums.leitura.CondicaoSaude;
import com.springwalker.back.monitoramento.enums.leitura.Gravidade;
import com.springwalker.back.monitoramento.enums.leitura.TipoDado;
import com.springwalker.back.monitoramento.enums.leitura.UnidadeMedida;
import com.springwalker.back.monitoramento.enums.notificacao.StatusNotificacao;
import com.springwalker.back.monitoramento.model.LeituraSensor;
import com.springwalker.back.monitoramento.model.Notificacao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@DisplayName("Testes para NotificacaoMapper")
class NotificacaoMapperTest {

    @Autowired
    private NotificacaoMapper mapper;

    @Test
    @DisplayName("Deve mapear Notificacao para NotificacaoResponseDTO corretamente")
    void toResponse_shouldMapNotificacaoToNotificacaoResponseDTO() {
        // Given
        Atendimento atendimento = new Atendimento();
        atendimento.setId(10L);
        atendimento.setNumeroQuarto(101);

        LeituraSensor leituraSensor = LeituraSensor.builder()
                .id(1L)
                .valor(38.5)
                .dataHora(LocalDateTime.of(2023, 1, 15, 10, 0))
                .tipoDado(TipoDado.TEMPERATURA)
                .unidadeMedida(UnidadeMedida.CELSIUS)
                .atendimento(atendimento)
                .gravidade(Gravidade.ALERTA)
                .condicaoSaude(CondicaoSaude.NORMAL)
                .duracaoEstimadaMinutos(60)
                .build();

        Notificacao notificacao = Notificacao.builder()
                .id(100L)
                .leituraSensor(leituraSensor)
                .status(StatusNotificacao.ABERTA)
                .dataCriacao(LocalDateTime.of(2023, 1, 15, 10, 5))
                .numeroQuarto(atendimento.getNumeroQuarto())
                .build();

        // When
        NotificacaoResponseDTO dto = mapper.toResponse(notificacao);

        // Then
        assertNotNull(dto);
        assertEquals(notificacao.getId(), dto.getId());
        assertEquals(notificacao.getStatus(), dto.getStatus());
        assertEquals(notificacao.getDataCriacao(), dto.getDataCriacao());
        assertEquals(notificacao.getDataFechamento(), dto.getDataFechamento());
        assertEquals(notificacao.getNumeroQuarto(), dto.getNumeroQuarto());

        // Assert LeituraSensor details
        assertNotNull(dto.getLeituraSensor());
        assertEquals(leituraSensor.getId(), dto.getLeituraSensor().getId());
        assertEquals(leituraSensor.getValor(), dto.getLeituraSensor().getValor());
        assertEquals(leituraSensor.getDataHora(), dto.getLeituraSensor().getDataHora());
        assertEquals(leituraSensor.getTipoDado(), dto.getLeituraSensor().getTipoDado());
        assertEquals(leituraSensor.getUnidadeMedida(), dto.getLeituraSensor().getUnidadeMedida());
        assertEquals(leituraSensor.getGravidade(), dto.getLeituraSensor().getGravidade());
        assertEquals(leituraSensor.getCondicaoSaude(), dto.getLeituraSensor().getCondicaoSaude());
        assertEquals(leituraSensor.getDuracaoEstimadaMinutos(), dto.getLeituraSensor().getDuracaoEstimadaMinutos());
        assertEquals(atendimento.getId(), dto.getLeituraSensor().getAtendimentoId()); // Corrected assertion
    }

    @Test
    @DisplayName("Deve lidar com Notificacao nula")
    void toResponse_shouldHandleNullNotificacao() {
        // When
        NotificacaoResponseDTO dto = mapper.toResponse(null);

        // Then
        assertNull(dto);
    }
}
