package com.springwalker.back.monitoramento.services.leitura.processamento.strategies;

import com.springwalker.back.core.enums.TipoDado;
import com.springwalker.back.monitoramento.model.LeituraSensor;

public interface AnaliseDadosSensorStrategy {
    TipoDado getSupportedTipoDado();
    void analisar(LeituraSensor leitura);
}
