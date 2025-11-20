package com.springwalker.back.monitoramento.service.leitura.processamento;

import com.springwalker.back.monitoramento.enums.leitura.TipoDado;
import com.springwalker.back.monitoramento.model.LeituraSensor;
import com.springwalker.back.monitoramento.service.leitura.processamento.strategies.AnaliseDadosSensorStrategy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para AnaliseDadosSensorService")
class AnaliseDadosSensorServiceTest {

    @Mock
    private AnaliseDadosSensorStrategy temperaturaStrategy;
    @Mock
    private AnaliseDadosSensorStrategy pressaoArterialStrategy;
    @Mock
    private AnaliseDadosSensorStrategy medicacaoStrategy;

    @Test
    @DisplayName("Deve inicializar o serviço com as estratégias corretas")
    void constructor_shouldInitializeStrategiesCorrectly() {
        when(temperaturaStrategy.getSupportedTipoDado()).thenReturn(TipoDado.TEMPERATURA);
        when(pressaoArterialStrategy.getSupportedTipoDado()).thenReturn(TipoDado.PRESSAO_ARTERIAL); // Corrigido
        when(medicacaoStrategy.getSupportedTipoDado()).thenReturn(TipoDado.MEDICACAO);

        List<AnaliseDadosSensorStrategy> strategies = Arrays.asList(temperaturaStrategy, pressaoArterialStrategy, medicacaoStrategy);
        AnaliseDadosSensorService service = new AnaliseDadosSensorService(strategies);

        LeituraSensor temperaturaLeitura = new LeituraSensor();
        temperaturaLeitura.setTipoDado(TipoDado.TEMPERATURA);
        service.analisarDadosSensor(temperaturaLeitura);
        verify(temperaturaStrategy, times(1)).analisar(temperaturaLeitura);

        LeituraSensor pressaoLeitura = new LeituraSensor();
        pressaoLeitura.setTipoDado(TipoDado.PRESSAO_ARTERIAL); // Corrigido
        service.analisarDadosSensor(pressaoLeitura);
        verify(pressaoArterialStrategy, times(1)).analisar(pressaoLeitura);

        LeituraSensor medicacaoLeitura = new LeituraSensor();
        medicacaoLeitura.setTipoDado(TipoDado.MEDICACAO);
        service.analisarDadosSensor(medicacaoLeitura);
        verify(medicacaoStrategy, times(1)).analisar(medicacaoLeitura);
    }

    @Test
    @DisplayName("Deve chamar a estratégia correta para o tipo de dado")
    void analisarDadosSensor_shouldCallCorrectStrategy() {
        when(temperaturaStrategy.getSupportedTipoDado()).thenReturn(TipoDado.TEMPERATURA);
        List<AnaliseDadosSensorStrategy> strategies = Collections.singletonList(temperaturaStrategy);
        AnaliseDadosSensorService service = new AnaliseDadosSensorService(strategies);

        LeituraSensor leitura = new LeituraSensor();
        leitura.setTipoDado(TipoDado.TEMPERATURA);

        service.analisarDadosSensor(leitura);

        verify(temperaturaStrategy, times(1)).analisar(leitura);
        verify(pressaoArterialStrategy, times(0)).analisar(any()); // 'any()' importado
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException para tipo de dado sem estratégia")
    void analisarDadosSensor_shouldThrowExceptionForUnsupportedTipoDado() {
        // Only provide a strategy for TEMPERATURA
        when(temperaturaStrategy.getSupportedTipoDado()).thenReturn(TipoDado.TEMPERATURA);
        List<AnaliseDadosSensorStrategy> strategies = Collections.singletonList(temperaturaStrategy);
        AnaliseDadosSensorService service = new AnaliseDadosSensorService(strategies);

        LeituraSensor leitura = new LeituraSensor();
        leitura.setTipoDado(TipoDado.FREQUENCIA_CARDIACA); // No strategy for this type

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                service.analisarDadosSensor(leitura));

        assertEquals("Nenhuma estratégia de análise encontrada para o tipo de dado: " + TipoDado.FREQUENCIA_CARDIACA, exception.getMessage());
        verify(temperaturaStrategy, times(0)).analisar(any()); // 'any()' importado
    }
}
