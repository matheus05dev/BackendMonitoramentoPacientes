package com.springwalker.back.monitoramento.service.leitura.processamento.strategies;

import com.springwalker.back.monitoramento.enums.leitura.CondicaoSaude;
import com.springwalker.back.monitoramento.enums.leitura.Gravidade;
import com.springwalker.back.monitoramento.enums.leitura.TipoDado;
import com.springwalker.back.monitoramento.enums.notificacao.StatusNotificacao;
import com.springwalker.back.monitoramento.model.LeituraSensor;
import com.springwalker.back.monitoramento.model.Notificacao;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MedicacaoAnaliseStrategy implements AnaliseDadosSensorStrategy {

    @Override
    public TipoDado getSupportedTipoDado() {
        return TipoDado.MEDICACAO;
    }

    @Override
    public void analisar(LeituraSensor leitura) {
        if (leitura.getDuracaoEstimadaMinutos() != null && leitura.getDuracaoEstimadaMinutos() > 0) {
            leitura.setCondicaoSaude(CondicaoSaude.MEDICACAO_FINALIZANDO);
            leitura.setGravidade(Gravidade.ALERTA);

            LocalDateTime dataHoraLeitura = leitura.getDataHora();
            LocalDateTime dataHoraNotificacao = dataHoraLeitura.plusMinutes(leitura.getDuracaoEstimadaMinutos());

            StatusNotificacao statusNotificacao;
            if (leitura.getDuracaoEstimadaMinutos() <= 2) {
                statusNotificacao = StatusNotificacao.FECHADA;
            } else {
                statusNotificacao = StatusNotificacao.PENDENTE;
            }

            Notificacao notificacao = Notificacao.builder()
                    .leituraSensor(leitura)
                    .status(statusNotificacao)
                    .dataCriacao(dataHoraNotificacao)
                    .numeroQuarto(leitura.getAtendimento().getQuarto().getNumero())
                    .build();

            leitura.setNotificacao(notificacao);
        } else {
            leitura.setCondicaoSaude(CondicaoSaude.NORMAL);
            leitura.setGravidade(Gravidade.NORMAL);
        }
    }
}
