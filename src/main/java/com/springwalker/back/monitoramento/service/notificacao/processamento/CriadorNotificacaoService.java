package com.springwalker.back.monitoramento.service.notificacao.processamento;

import com.springwalker.back.core.enums.StatusNotificacao;
import com.springwalker.back.monitoramento.model.LeituraSensor;
import com.springwalker.back.monitoramento.model.Notificacao;
import com.springwalker.back.monitoramento.repository.NotificacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CriadorNotificacaoService {

    private final NotificacaoRepository notificacaoRepository;

    public Notificacao criarEGravarNotificacao(LeituraSensor leitura) {
        Notificacao notificacao = Notificacao.builder()
                .leituraSensor(leitura)
                .status(StatusNotificacao.ABERTA)
                .dataCriacao(LocalDateTime.now())
                .build();
        return notificacaoRepository.save(notificacao);
    }
}
