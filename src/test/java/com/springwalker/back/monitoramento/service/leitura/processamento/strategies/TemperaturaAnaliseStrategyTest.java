package com.springwalker.back.monitoramento.service.leitura.processamento.strategies;

import com.springwalker.back.monitoramento.enums.leitura.CondicaoSaude;
import com.springwalker.back.monitoramento.enums.leitura.Gravidade;
import com.springwalker.back.monitoramento.enums.leitura.TipoDado;
import com.springwalker.back.monitoramento.model.LeituraSensor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Testes para TemperaturaAnaliseStrategy")
class TemperaturaAnaliseStrategyTest {

    private TemperaturaAnaliseStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new TemperaturaAnaliseStrategy();
    }

    @Test
    @DisplayName("Deve retornar TipoDado.TEMPERATURA como tipo suportado")
    void getSupportedTipoDado_shouldReturnTemperatura() {
        assertEquals(TipoDado.TEMPERATURA, strategy.getSupportedTipoDado());
    }

    @Test
    @DisplayName("Deve analisar temperatura < 35 (Hipotermia Severa, Emergencial)")
    void analisar_shouldHandleSevereHypothermia() {
        LeituraSensor leitura = LeituraSensor.builder().valor(34.9).build();
        strategy.analisar(leitura);
        assertEquals(CondicaoSaude.HIPORTEMIA_SEVERA, leitura.getCondicaoSaude());
        assertEquals(Gravidade.EMERGENCIAL, leitura.getGravidade());
    }

    @Test
    @DisplayName("Deve analisar temperatura entre 35 e 36 (Hipotermia, Alerta)")
    void analisar_shouldHandleHypothermia() {
        LeituraSensor leitura = LeituraSensor.builder().valor(35.5).build();
        strategy.analisar(leitura);
        assertEquals(CondicaoSaude.HIPOTERMIA, leitura.getCondicaoSaude());
        assertEquals(Gravidade.ALERTA, leitura.getGravidade());
    }

    @Test
    @DisplayName("Deve analisar temperatura entre 36.1 e 37.2 (Normal, Normal)")
    void analisar_shouldHandleNormalTemperature() {
        LeituraSensor leitura = LeituraSensor.builder().valor(36.8).build();
        strategy.analisar(leitura);
        assertEquals(CondicaoSaude.NORMAL, leitura.getCondicaoSaude());
        assertEquals(Gravidade.NORMAL, leitura.getGravidade());
    }

    @Test
    @DisplayName("Deve analisar temperatura entre 37.3 e 37.7 (Estado Febril, Alerta)")
    void analisar_shouldHandleFebrileState() {
        LeituraSensor leitura = LeituraSensor.builder().valor(37.5).build();
        strategy.analisar(leitura);
        assertEquals(CondicaoSaude.ESTADO_FEBRIL, leitura.getCondicaoSaude());
        assertEquals(Gravidade.ALERTA, leitura.getGravidade());
    }

    @Test
    @DisplayName("Deve analisar temperatura entre 37.8 e 38.9 (Febre, Alerta)")
    void analisar_shouldHandleFever() {
        LeituraSensor leitura = LeituraSensor.builder().valor(38.2).build();
        strategy.analisar(leitura);
        assertEquals(CondicaoSaude.FEBRE, leitura.getCondicaoSaude());
        assertEquals(Gravidade.ALERTA, leitura.getGravidade());
    }

    @Test
    @DisplayName("Deve analisar temperatura > 39 (Febre Alta, Emergencial)")
    void analisar_shouldHandleHighFever() {
        LeituraSensor leitura = LeituraSensor.builder().valor(39.1).build();
        strategy.analisar(leitura);
        assertEquals(CondicaoSaude.FEBRE_ALTA, leitura.getCondicaoSaude());
        assertEquals(Gravidade.EMERGENCIAL, leitura.getGravidade());
    }
}
