package com.springwalker.back.monitoramento.services.notificacao.processamento;

import com.springwalker.back.core.enums.Gravidade;
import com.springwalker.back.core.enums.StatusNotificacao;
import com.springwalker.back.monitoramento.model.LeituraSensor;
import com.springwalker.back.monitoramento.model.Notificacao;
import com.springwalker.back.monitoramento.repository.NotificacaoRepository;
import com.springwalker.back.monitoramento.services.notificacao.NotificacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GerenciadorNotificacaoService {

    private final NotificacaoRepository notificacaoRepository;
    private final NotificacaoService notificacaoService;

    public void processarEEnviarNotificacao(LeituraSensor leitura) {
        if (deveNotificar(leitura.getGravidade())) {
            Notificacao notificacao = Notificacao.builder()
                    .leituraSensor(leitura)
                    .status(StatusNotificacao.ABERTA)
                    .dataCriacao(LocalDateTime.now())
                    .build();

            Notificacao savedNotificacao = notificacaoRepository.save(notificacao);

            notificacaoService.enviarNotificacao(savedNotificacao);
        }
    }

    public List<Notificacao> buscarTodasNotificacoes() {
        return notificacaoRepository.findAllByOrderByDataCriacaoDesc();
    }

    public List<Notificacao> buscarNotificacoesPorStatus(StatusNotificacao status) {
        return notificacaoRepository.findByStatus(status);
    }

    public Optional<Notificacao> fecharNotificacao(Long id) {
        Optional<Notificacao> notificacaoOpt = notificacaoRepository.findById(id);
        if (notificacaoOpt.isPresent()) {
            Notificacao notificacao = notificacaoOpt.get();
            if (notificacao.getStatus() != StatusNotificacao.FECHADA) {
                notificacao.setStatus(StatusNotificacao.FECHADA);
                notificacao.setDataFechamento(LocalDateTime.now());
                return Optional.of(notificacaoRepository.save(notificacao));
            }
        }
        return notificacaoOpt;
    }

    private boolean deveNotificar(Gravidade gravidade) {
        return gravidade != null && gravidade != Gravidade.NORMAL;
    }
}
