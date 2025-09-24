package com.springwalker.back.monitoramento.services.leitura.processamento.strategies;

import com.springwalker.back.core.enums.CondicaoSaude;
import com.springwalker.back.core.enums.Gravidade;
import com.springwalker.back.core.enums.TipoDado;
import com.springwalker.back.monitoramento.model.LeituraSensor;
import org.springframework.stereotype.Component;

@Component
public class PressaoArterialAnaliseStrategy implements AnaliseDadosSensorStrategy {

    @Override
    public TipoDado getSupportedTipoDado() {
        return TipoDado.PRESSAO_ARTERIAL;
    }

    @Override
    public void analisar(LeituraSensor leitura) {
        double valor = leitura.getValor();

        if (valor < 90) {
            leitura.setCondicaoSaude(CondicaoSaude.HIPOTENSAO);
            leitura.setGravidade(Gravidade.EMERGENCIAL);
        } else if (valor >= 90 && valor <= 120) {
            leitura.setCondicaoSaude(CondicaoSaude.NORMAL);
            leitura.setGravidade(Gravidade.NORMAL);
        } else if (valor >= 121 && valor <= 139) {
            leitura.setCondicaoSaude(CondicaoSaude.PRE_HIPERTENSAO);
            leitura.setGravidade(Gravidade.ALERTA);
        } else if (valor >= 140 && valor <= 159) {
            leitura.setCondicaoSaude(CondicaoSaude.HIPERTENSAO_ESTAGIO_1);
            leitura.setGravidade(Gravidade.ALERTA);
        } else if (valor >= 160 && valor <= 180) {
            leitura.setCondicaoSaude(CondicaoSaude.HIPERTENSAO_ESTAGIO_2);
            leitura.setGravidade(Gravidade.EMERGENCIAL);
        } else if (valor > 180) {
            leitura.setCondicaoSaude(CondicaoSaude.CRISE_HIPERTENSIVA);
            leitura.setGravidade(Gravidade.EMERGENCIAL);
        }
    }
}
