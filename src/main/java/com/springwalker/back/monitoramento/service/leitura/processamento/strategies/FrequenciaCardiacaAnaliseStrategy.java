package com.springwalker.back.monitoramento.service.leitura.processamento.strategies;

import com.springwalker.back.core.enums.CondicaoSaude;
import com.springwalker.back.core.enums.Gravidade;
import com.springwalker.back.core.enums.TipoDado;
import com.springwalker.back.monitoramento.model.LeituraSensor;
import org.springframework.stereotype.Component;

@Component
public class FrequenciaCardiacaAnaliseStrategy implements AnaliseDadosSensorStrategy {

    @Override
    public TipoDado getSupportedTipoDado() {
        return TipoDado.FREQUENCIA_CARDIACA;
    }

    @Override
    public void analisar(LeituraSensor leitura) {
        double valor = leitura.getValor();

        if (valor < 60) {
            leitura.setCondicaoSaude(CondicaoSaude.BRADICARDIA);
            leitura.setGravidade(Gravidade.ALERTA);
        } else if (valor >= 60 && valor <= 100) {
            leitura.setCondicaoSaude(CondicaoSaude.NORMAL);
            leitura.setGravidade(Gravidade.NORMAL);
        } else {
            leitura.setCondicaoSaude(CondicaoSaude.TAQUICARDIA);
            leitura.setGravidade(Gravidade.ALERTA);
        }
    }
}
