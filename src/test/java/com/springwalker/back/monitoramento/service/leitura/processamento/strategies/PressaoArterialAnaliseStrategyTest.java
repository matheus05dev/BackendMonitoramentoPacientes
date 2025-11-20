package com.springwalker.back.monitoramento.service.leitura.processamento.strategies;

import com.springwalker.back.monitoramento.enums.leitura.CondicaoSaude;
import com.springwalker.back.monitoramento.enums.leitura.Gravidade;
import com.springwalker.back.monitoramento.enums.leitura.TipoDado;
import com.springwalker.back.monitoramento.model.LeituraSensor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Testes para PressaoArterialAnaliseStrategy")
class PressaoArterialAnaliseStrategyTest {

    private PressaoArterialAnaliseStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new PressaoArterialAnaliseStrategy();
    }

    @Test
    @DisplayName("Deve retornar TipoDado.PRESSAO_ARTERIAL como tipo suportado")
    void getSupportedTipoDado_shouldReturnPressaoArterial() {
        assertEquals(TipoDado.PRESSAO_ARTERIAL, strategy.getSupportedTipoDado());
    }

    @Test
    @DisplayName("Deve analisar pressão arterial < 90 (Hipotensão, Emergencial)")
    void analisar_shouldHandleHypotension() {
        LeituraSensor leitura = LeituraSensor.builder().valor(85.0).build();
        strategy.analisar(leitura);
        assertEquals(CondicaoSaude.HIPOTENSAO, leitura.getCondicaoSaude());
        assertEquals(Gravidade.EMERGENCIAL, leitura.getGravidade());
    }

    @Test
    @DisplayName("Deve analisar pressão arterial entre 90 e 120 (Normal, Normal)")
    void analisar_shouldHandleNormalBloodPressure() {
        LeituraSensor leitura = LeituraSensor.builder().valor(110.0).build();
        strategy.analisar(leitura);
        assertEquals(CondicaoSaude.NORMAL, leitura.getCondicaoSaude());
        assertEquals(Gravidade.NORMAL, leitura.getGravidade());
    }

    @Test
    @DisplayName("Deve analisar pressão arterial entre 121 e 139 (Pré-Hipertensão, Alerta)")
    void analisar_shouldHandlePreHypertension() {
        LeituraSensor leitura = LeituraSensor.builder().valor(130.0).build();
        strategy.analisar(leitura);
        assertEquals(CondicaoSaude.PRE_HIPERTENSAO, leitura.getCondicaoSaude());
        assertEquals(Gravidade.ALERTA, leitura.getGravidade());
    }

    @Test
    @DisplayName("Deve analisar pressão arterial entre 140 e 159 (Hipertensão Estágio 1, Alerta)")
    void analisar_shouldHandleHypertensionStage1() {
        LeituraSensor leitura = LeituraSensor.builder().valor(150.0).build();
        strategy.analisar(leitura);
        assertEquals(CondicaoSaude.HIPERTENSAO_ESTAGIO_1, leitura.getCondicaoSaude());
        assertEquals(Gravidade.ALERTA, leitura.getGravidade());
    }

    @Test
    @DisplayName("Deve analisar pressão arterial entre 160 e 180 (Hipertensão Estágio 2, Emergencial)")
    void analisar_shouldHandleHypertensionStage2() {
        LeituraSensor leitura = LeituraSensor.builder().valor(170.0).build();
        strategy.analisar(leitura);
        assertEquals(CondicaoSaude.HIPERTENSAO_ESTAGIO_2, leitura.getCondicaoSaude());
        assertEquals(Gravidade.EMERGENCIAL, leitura.getGravidade());
    }

    @Test
    @DisplayName("Deve analisar pressão arterial > 180 (Crise Hipertensiva, Emergencial)")
    void analisar_shouldHandleHypertensiveCrisis() {
        LeituraSensor leitura = LeituraSensor.builder().valor(190.0).build();
        strategy.analisar(leitura);
        assertEquals(CondicaoSaude.CRISE_HIPERTENSIVA, leitura.getCondicaoSaude());
        assertEquals(Gravidade.EMERGENCIAL, leitura.getGravidade());
    }
}
