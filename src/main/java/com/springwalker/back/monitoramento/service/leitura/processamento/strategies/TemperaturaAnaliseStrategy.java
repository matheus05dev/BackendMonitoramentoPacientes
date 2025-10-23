package com.springwalker.back.monitoramento.service.leitura.processamento.strategies;

import com.springwalker.back.monitoramento.enums.leitura.CondicaoSaude;
import com.springwalker.back.monitoramento.enums.leitura.Gravidade;
import com.springwalker.back.monitoramento.enums.leitura.TipoDado;
import com.springwalker.back.monitoramento.model.LeituraSensor;
import org.springframework.stereotype.Component;

@Component
public class TemperaturaAnaliseStrategy implements AnaliseDadosSensorStrategy {

    @Override
    public TipoDado getSupportedTipoDado() {
        return TipoDado.TEMPERATURA;
    }

    @Override
    public void analisar(LeituraSensor leitura) {
        double valor = leitura.getValor();
        if(valor <35){
            leitura.setCondicaoSaude(CondicaoSaude.HIPORTEMIA_SEVERA);
            leitura.setGravidade(Gravidade.EMERGENCIAL);
        } else if(valor <= 36) {
            leitura.setCondicaoSaude(CondicaoSaude.HIPOTERMIA);
            leitura.setGravidade(Gravidade.ALERTA);
        } else if (valor >= 36.1 && valor <= 37.2) {
            leitura.setCondicaoSaude(CondicaoSaude.NORMAL);
            leitura.setGravidade(Gravidade.NORMAL);
        } else if (valor >= 37.3 && valor <= 37.7) {
            leitura.setCondicaoSaude(CondicaoSaude.ESTADO_FEBRIL);
            leitura.setGravidade(Gravidade.ALERTA);
        } else if (valor >= 37.8 && valor <= 38.9) {
            leitura.setCondicaoSaude(CondicaoSaude.FEBRE);
            leitura.setGravidade(Gravidade.ALERTA);
        } else if (valor > 39) {
            leitura.setCondicaoSaude(CondicaoSaude.FEBRE_ALTA);
            leitura.setGravidade(Gravidade.EMERGENCIAL);
        }
    }
}
