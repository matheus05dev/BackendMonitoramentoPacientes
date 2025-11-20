package com.springwalker.back.monitoramento.service.leitura.processamento.strategies;

import com.springwalker.back.atendimento.model.Atendimento;
import com.springwalker.back.monitoramento.enums.leitura.CondicaoSaude;
import com.springwalker.back.monitoramento.enums.leitura.Gravidade;
import com.springwalker.back.monitoramento.enums.leitura.TipoDado;
import com.springwalker.back.monitoramento.enums.notificacao.StatusNotificacao;
import com.springwalker.back.monitoramento.model.LeituraSensor;
import com.springwalker.back.monitoramento.model.Notificacao;
import com.springwalker.back.quarto.model.Quarto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("Testes para MedicacaoAnaliseStrategy")
class MedicacaoAnaliseStrategyTest {

    private MedicacaoAnaliseStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new MedicacaoAnaliseStrategy();
    }

    @Test
    @DisplayName("Deve retornar TipoDado.MEDICACAO como tipo suportado")
    void getSupportedTipoDado_shouldReturnMedicacao() {
        assertEquals(TipoDado.MEDICACAO, strategy.getSupportedTipoDado());
    }

    @Test
    @DisplayName("Deve analisar leitura de medicação com duração positiva e criar notificação PENDENTE")
    void analisar_shouldProcessMedicacaoWithPositiveDurationAndCreatePendingNotification() {
        com.springwalker.back.quarto.model.Quarto quarto = new Quarto();
        quarto.setNumero(101);

        Atendimento atendimento = new Atendimento();
        atendimento.setQuarto(quarto);

        LocalDateTime dataHoraLeitura = LocalDateTime.now();
        LeituraSensor leitura = LeituraSensor.builder()
                .valor(10.0)
                .dataHora(dataHoraLeitura)
                .tipoDado(TipoDado.MEDICACAO)
                .duracaoEstimadaMinutos(30)
                .atendimento(atendimento)
                .build();

        strategy.analisar(leitura);

        assertEquals(CondicaoSaude.MEDICACAO_FINALIZANDO, leitura.getCondicaoSaude());
        assertEquals(Gravidade.ALERTA, leitura.getGravidade());
        assertNotNull(leitura.getNotificacao());
        assertEquals(StatusNotificacao.PENDENTE, leitura.getNotificacao().getStatus());
        assertEquals(dataHoraLeitura.plusMinutes(30), leitura.getNotificacao().getDataCriacao());
        assertEquals(quarto.getNumero(), leitura.getNotificacao().getNumeroQuarto());
        assertEquals(leitura, leitura.getNotificacao().getLeituraSensor());
    }

    @Test
    @DisplayName("Deve analisar leitura de medicação com duração positiva e criar notificação FECHADA (duração <= 2)")
    void analisar_shouldProcessMedicacaoWithShortDurationAndCreateClosedNotification() {
        Quarto quarto = new Quarto();
        quarto.setNumero(102);

        Atendimento atendimento = new Atendimento();
        atendimento.setQuarto(quarto);

        LocalDateTime dataHoraLeitura = LocalDateTime.now();
        LeituraSensor leitura = LeituraSensor.builder()
                .valor(5.0)
                .dataHora(dataHoraLeitura)
                .tipoDado(TipoDado.MEDICACAO)
                .duracaoEstimadaMinutos(2)
                .atendimento(atendimento)
                .build();

        strategy.analisar(leitura);

        assertEquals(CondicaoSaude.MEDICACAO_FINALIZANDO, leitura.getCondicaoSaude());
        assertEquals(Gravidade.ALERTA, leitura.getGravidade());
        assertNotNull(leitura.getNotificacao());
        assertEquals(StatusNotificacao.FECHADA, leitura.getNotificacao().getStatus());
        assertEquals(dataHoraLeitura.plusMinutes(2), leitura.getNotificacao().getDataCriacao());
        assertEquals(quarto.getNumero(), leitura.getNotificacao().getNumeroQuarto());
        assertEquals(leitura, leitura.getNotificacao().getLeituraSensor());
    }

    @Test
    @DisplayName("Deve analisar leitura de medicação sem duração estimada e definir status NORMAL")
    void analisar_shouldProcessMedicacaoWithoutDurationAndSetNormalStatus() {
        LeituraSensor leitura = LeituraSensor.builder()
                .valor(1.0)
                .dataHora(LocalDateTime.now())
                .tipoDado(TipoDado.MEDICACAO)
                .duracaoEstimadaMinutos(0) // or null
                .build();

        strategy.analisar(leitura);

        assertEquals(CondicaoSaude.NORMAL, leitura.getCondicaoSaude());
        assertEquals(Gravidade.NORMAL, leitura.getGravidade());
        assertNull(leitura.getNotificacao());
    }

    @Test
    @DisplayName("Deve analisar leitura de medicação com duração nula e definir status NORMAL")
    void analisar_shouldProcessMedicacaoWithNullDurationAndSetNormalStatus() {
        LeituraSensor leitura = LeituraSensor.builder()
                .valor(1.0)
                .dataHora(LocalDateTime.now())
                .tipoDado(TipoDado.MEDICACAO)
                .duracaoEstimadaMinutos(null)
                .build();

        strategy.analisar(leitura);

        assertEquals(CondicaoSaude.NORMAL, leitura.getCondicaoSaude());
        assertEquals(Gravidade.NORMAL, leitura.getGravidade());
        assertNull(leitura.getNotificacao());
    }
}
