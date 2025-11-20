package com.springwalker.back.monitoramento.service.leitura.processamento.strategies;

import com.springwalker.back.monitoramento.enums.leitura.CondicaoSaude;
import com.springwalker.back.monitoramento.enums.leitura.Gravidade;
import com.springwalker.back.monitoramento.enums.leitura.TipoDado;
import com.springwalker.back.monitoramento.model.LeituraSensor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Testes para FrequenciaCardiacaAnaliseStrategy")
class FrequenciaCardiacaAnaliseStrategyTest {

    private FrequenciaCardiacaAnaliseStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new FrequenciaCardiacaAnaliseStrategy();
    }

    @Test
    @DisplayName("Deve retornar TipoDado.FREQUENCIA_CARDIACA como tipo suportado")
    void getSupportedTipoDado_shouldReturnFrequenciaCardiaca() {
        assertEquals(TipoDado.FREQUENCIA_CARDIACA, strategy.getSupportedTipoDado());
    }

    @Test
    @DisplayName("Deve analisar frequência cardíaca < 60 (Bradicardia, Alerta)")
    void analisar_shouldHandleBradycardia() {
        LeituraSensor leitura = LeituraSensor.builder().valor(55.0).build();
        strategy.analisar(leitura);
        assertEquals(CondicaoSaude.BRADICARDIA, leitura.getCondicaoSaude());
        assertEquals(Gravidade.ALERTA, leitura.getGravidade());
    }

    @Test
    @DisplayName("Deve analisar frequência cardíaca entre 60 e 100 (Normal, Normal)")
    void analisar_shouldHandleNormalHeartRate() {
        LeituraSensor leitura = LeituraSensor.builder().valor(80.0).build();
        strategy.analisar(leitura);
        assertEquals(CondicaoSaude.NORMAL, leitura.getCondicaoSaude());
        assertEquals(Gravidade.NORMAL, leitura.getGravidade());
    }

    @Test
    @DisplayName("Deve analisar frequência cardíaca > 100 (Taquicardia, Alerta)")
    void analisar_shouldHandleTachycardia() {
        LeituraSensor leitura = LeituraSensor.builder().valor(110.0).build();
        strategy.analisar(leitura);
        assertEquals(CondicaoSaude.TAQUICARDIA, leitura.getCondicaoSaude());
        assertEquals(Gravidade.ALERTA, leitura.getGravidade());
    }
}
