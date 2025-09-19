package com.springwalker.back.monitoramento.services.processamento;

import com.springwalker.back.core.enums.CondicaoSaude;
import com.springwalker.back.core.enums.Gravidade;
import com.springwalker.back.core.enums.TipoDado;
import com.springwalker.back.monitoramento.model.LeituraSensor;
import org.springframework.stereotype.Service;

@Service
public class AnaliseDadosSensorService {

    public void analisarDadosSensor(LeituraSensor leitura) {
        TipoDado tipoDado = leitura.getTipoDado();
        double valor = leitura.getValor();

        if (tipoDado == TipoDado.TEMPERATURA) {
            if (valor < 35) {
                leitura.setCondicaoSaude(CondicaoSaude.HIPOTERMIA);
                leitura.setGravidade(Gravidade.EMERGENCIAL);
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
        } else if (tipoDado == TipoDado.FREQUENCIA_CARDIACA) {
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
        } else if (tipoDado == TipoDado.PRESSAO_ARTERIAL) {
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
}
