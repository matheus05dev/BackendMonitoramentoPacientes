package com.springwalker.back.monitoramento.service.notificacao.processamento;

import com.springwalker.back.core.enums.StatusNotificacao;
import com.springwalker.back.monitoramento.model.Notificacao;
import com.springwalker.back.monitoramento.repository.NotificacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FechadorNotificacaoService {

    private final NotificacaoRepository notificacaoRepository;

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
}
