package com.springwalker.back.monitoramento.model;

import com.springwalker.back.monitoramento.enums.notificacao.StatusNotificacao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Testes para o Modelo Notificacao")
class NotificacaoTest {

    @Test
    @DisplayName("Deve criar uma Notificacao usando o construtor e verificar os getters")
    void constructorAndGettersTest() {
        Long id = 1L;
        LeituraSensor leituraSensor = new LeituraSensor(); // Mock or create a simple LeituraSensor
        StatusNotificacao status = StatusNotificacao.ABERTA;
        LocalDateTime dataCriacao = LocalDateTime.now();
        LocalDateTime dataFechamento = LocalDateTime.now().plusHours(1);
        Integer numeroQuarto = 101;

        Notificacao notificacao = new Notificacao(id, leituraSensor, status, dataCriacao, dataFechamento, numeroQuarto);

        assertNotNull(notificacao);
        assertEquals(id, notificacao.getId());
        assertEquals(leituraSensor, notificacao.getLeituraSensor());
        assertEquals(status, notificacao.getStatus());
        assertEquals(dataCriacao, notificacao.getDataCriacao());
        assertEquals(dataFechamento, notificacao.getDataFechamento());
        assertEquals(numeroQuarto, notificacao.getNumeroQuarto());
    }

    @Test
    @DisplayName("Deve criar uma Notificacao usando o builder e verificar os getters")
    void builderAndGettersTest() {
        Long id = 2L;
        LeituraSensor leituraSensor = new LeituraSensor(); // Mock or create a simple LeituraSensor
        StatusNotificacao status = StatusNotificacao.EM_ATENDIMENTO;
        LocalDateTime dataCriacao = LocalDateTime.now().minusDays(1);
        Integer numeroQuarto = 102;

        Notificacao notificacao = Notificacao.builder()
                .id(id)
                .leituraSensor(leituraSensor)
                .status(status)
                .dataCriacao(dataCriacao)
                .numeroQuarto(numeroQuarto)
                .build();

        assertNotNull(notificacao);
        assertEquals(id, notificacao.getId());
        assertEquals(leituraSensor, notificacao.getLeituraSensor());
        assertEquals(status, notificacao.getStatus());
        assertEquals(dataCriacao, notificacao.getDataCriacao());
        assertEquals(numeroQuarto, notificacao.getNumeroQuarto());
    }

    @Test
    @DisplayName("Deve testar os setters e getters da Notificacao")
    void settersAndGettersTest() {
        Notificacao notificacao = new Notificacao();

        Long id = 3L;
        LeituraSensor leituraSensor = new LeituraSensor();
        StatusNotificacao status = StatusNotificacao.FECHADA;
        LocalDateTime dataCriacao = LocalDateTime.now().minusHours(2);
        LocalDateTime dataFechamento = LocalDateTime.now().minusHours(1);
        Integer numeroQuarto = 103;

        notificacao.setId(id);
        notificacao.setLeituraSensor(leituraSensor);
        notificacao.setStatus(status);
        notificacao.setDataCriacao(dataCriacao);
        notificacao.setDataFechamento(dataFechamento);
        notificacao.setNumeroQuarto(numeroQuarto);

        assertEquals(id, notificacao.getId());
        assertEquals(leituraSensor, notificacao.getLeituraSensor());
        assertEquals(status, notificacao.getStatus());
        assertEquals(dataCriacao, notificacao.getDataCriacao());
        assertEquals(dataFechamento, notificacao.getDataFechamento());
        assertEquals(numeroQuarto, notificacao.getNumeroQuarto());
    }
}
