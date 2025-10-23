package com.springwalker.back.monitoramento.service.leitura.processamento.strategies;

import com.springwalker.back.monitoramento.enums.leitura.TipoDado;
import com.springwalker.back.monitoramento.model.LeituraSensor;

public interface AnaliseDadosSensorStrategy {
    TipoDado getSupportedTipoDado();
    void analisar(LeituraSensor leitura);
}
