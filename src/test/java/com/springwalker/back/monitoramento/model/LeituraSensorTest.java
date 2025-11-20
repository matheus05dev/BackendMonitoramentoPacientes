package com.springwalker.back.monitoramento.model;

import com.springwalker.back.atendimento.model.Atendimento;
import com.springwalker.back.monitoramento.enums.leitura.CondicaoSaude;
import com.springwalker.back.monitoramento.enums.leitura.Gravidade;
import com.springwalker.back.monitoramento.enums.leitura.TipoDado;
import com.springwalker.back.monitoramento.enums.leitura.UnidadeMedida;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Testes para o Modelo LeituraSensor")
class LeituraSensorTest {

    @Test
    @DisplayName("Deve criar uma LeituraSensor usando o construtor e verificar os getters")
    void constructorAndGettersTest() {
        Long id = 1L;
        Double valor = 36.5;
        LocalDateTime dataHora = LocalDateTime.now();
        TipoDado tipoDado = TipoDado.TEMPERATURA;
        UnidadeMedida unidadeMedida = UnidadeMedida.CELSIUS;
        Atendimento atendimento = new Atendimento();
        Gravidade gravidade = Gravidade.NORMAL;
        CondicaoSaude condicaoSaude = CondicaoSaude.FEBRE_ALTA;
        Notificacao notificacao = new Notificacao();
        Integer duracaoEstimadaMinutos = 60;

        LeituraSensor leituraSensor = new LeituraSensor(id, valor, dataHora, tipoDado, unidadeMedida, atendimento, gravidade, condicaoSaude, notificacao, duracaoEstimadaMinutos);

        assertNotNull(leituraSensor);
        assertEquals(id, leituraSensor.getId());
        assertEquals(valor, leituraSensor.getValor());
        assertEquals(dataHora, leituraSensor.getDataHora());
        assertEquals(tipoDado, leituraSensor.getTipoDado());
        assertEquals(unidadeMedida, leituraSensor.getUnidadeMedida());
        assertEquals(atendimento, leituraSensor.getAtendimento());
        assertEquals(gravidade, leituraSensor.getGravidade());
        assertEquals(condicaoSaude, leituraSensor.getCondicaoSaude());
        assertEquals(notificacao, leituraSensor.getNotificacao());
        assertEquals(duracaoEstimadaMinutos, leituraSensor.getDuracaoEstimadaMinutos());
    }

    @Test
    @DisplayName("Deve criar uma LeituraSensor usando o builder e verificar os getters")
    void builderAndGettersTest() {
        Long id = 2L;
        Double valor = 120.0;
        LocalDateTime dataHora = LocalDateTime.now().minusMinutes(30);
        TipoDado tipoDado = TipoDado.PRESSAO_ARTERIAL;
        UnidadeMedida unidadeMedida = UnidadeMedida.MMHG;
        Atendimento atendimento = new Atendimento();
        Gravidade gravidade = Gravidade.ALERTA;
        CondicaoSaude condicaoSaude = CondicaoSaude.HIPERTENSAO_ESTAGIO_1;
        Integer duracaoEstimadaMinutos = 30;

        LeituraSensor leituraSensor = LeituraSensor.builder()
                .id(id)
                .valor(valor)
                .dataHora(dataHora)
                .tipoDado(tipoDado)
                .unidadeMedida(unidadeMedida)
                .atendimento(atendimento)
                .gravidade(gravidade)
                .condicaoSaude(condicaoSaude)
                .duracaoEstimadaMinutos(duracaoEstimadaMinutos)
                .build();

        assertNotNull(leituraSensor);
        assertEquals(id, leituraSensor.getId());
        assertEquals(valor, leituraSensor.getValor());
        assertEquals(dataHora, leituraSensor.getDataHora());
        assertEquals(tipoDado, leituraSensor.getTipoDado());
        assertEquals(unidadeMedida, leituraSensor.getUnidadeMedida());
        assertEquals(atendimento, leituraSensor.getAtendimento());
        assertEquals(gravidade, leituraSensor.getGravidade());
        assertEquals(condicaoSaude, leituraSensor.getCondicaoSaude());
        assertEquals(duracaoEstimadaMinutos, leituraSensor.getDuracaoEstimadaMinutos());
    }

    @Test
    @DisplayName("Deve testar os setters e getters da LeituraSensor")
    void settersAndGettersTest() {
        LeituraSensor leituraSensor = new LeituraSensor();

        Long id = 3L;
        Double valor = 98.6;
        LocalDateTime dataHora = LocalDateTime.now().minusHours(1);
        TipoDado tipoDado = TipoDado.FREQUENCIA_CARDIACA;
        UnidadeMedida unidadeMedida = UnidadeMedida.BPM;
        Atendimento atendimento = new Atendimento();
        Gravidade gravidade = Gravidade.EMERGENCIAL;
        CondicaoSaude condicaoSaude = CondicaoSaude.TAQUICARDIA;
        Notificacao notificacao = new Notificacao();
        Integer duracaoEstimadaMinutos = 120;

        leituraSensor.setId(id);
        leituraSensor.setValor(valor);
        leituraSensor.setDataHora(dataHora);
        leituraSensor.setTipoDado(tipoDado);
        leituraSensor.setUnidadeMedida(unidadeMedida);
        leituraSensor.setAtendimento(atendimento);
        leituraSensor.setGravidade(gravidade);
        leituraSensor.setCondicaoSaude(condicaoSaude);
        leituraSensor.setNotificacao(notificacao);
        leituraSensor.setDuracaoEstimadaMinutos(duracaoEstimadaMinutos);

        assertEquals(id, leituraSensor.getId());
        assertEquals(valor, leituraSensor.getValor());
        assertEquals(dataHora, leituraSensor.getDataHora());
        assertEquals(tipoDado, leituraSensor.getTipoDado());
        assertEquals(unidadeMedida, leituraSensor.getUnidadeMedida());
        assertEquals(atendimento, leituraSensor.getAtendimento());
        assertEquals(gravidade, leituraSensor.getGravidade());
        assertEquals(condicaoSaude, leituraSensor.getCondicaoSaude());
        assertEquals(notificacao, leituraSensor.getNotificacao());
        assertEquals(duracaoEstimadaMinutos, leituraSensor.getDuracaoEstimadaMinutos());
    }
}
