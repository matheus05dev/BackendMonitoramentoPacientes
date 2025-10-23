package com.springwalker.back.monitoramento.service.notificacao.processamento;

import com.springwalker.back.monitoramento.enums.leitura.Gravidade;
import com.springwalker.back.monitoramento.model.LeituraSensor;
import com.springwalker.back.monitoramento.model.Notificacao;
import com.springwalker.back.monitoramento.service.notificacao.BuscarNotificacaoService;
import com.springwalker.back.monitoramento.service.notificacao.EnviadorNotificacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GerenciadorNotificacaoService {

    private final CriadorNotificacaoService criadorNotificacaoService;
    private final FechadorNotificacaoService fechadorNotificacaoService;
    private final EnviadorNotificacaoService enviadorNotificacaoService;
    private final BuscarNotificacaoService buscarNotificacaoService;

    public void processarEEnviarNotificacao(LeituraSensor leitura) {
        // 1. A leitura é grave o suficiente para justificar uma notificação?
        if (deveNotificar(leitura.getGravidade())) {

            // 2. Lógica de Fadiga de Alarme: Já existe um alarme aberto para este mesmo problema?
            Optional<Notificacao> notificacaoExistente = buscarNotificacaoService
                    .buscarNotificacaoAbertaPorAtendimentoETipoDado(leitura.getAtendimento().getId(), leitura.getTipoDado());

            // 3. Se não houver notificação aberta, crie e envie uma nova.
            if (notificacaoExistente.isEmpty()) {
                Notificacao savedNotificacao = criadorNotificacaoService.criarEGravarNotificacao(leitura);
                enviadorNotificacaoService.enviarNotificacao(savedNotificacao);
            }
            // Se já existir, não faz nada, suprimindo o alarme repetido.
        }
    }

    public Optional<Notificacao> fecharNotificacao(Long id) {
        return fechadorNotificacaoService.fecharNotificacao(id);
    }

    private boolean deveNotificar(Gravidade gravidade) {
        return gravidade != null && gravidade != Gravidade.NORMAL;
    }
}
