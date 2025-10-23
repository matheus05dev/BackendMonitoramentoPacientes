package com.springwalker.back.monitoramento.service.notificacao;

import com.springwalker.back.monitoramento.enums.notificacao.StatusNotificacao;
import com.springwalker.back.monitoramento.enums.leitura.TipoDado;
import com.springwalker.back.monitoramento.model.Notificacao;
import com.springwalker.back.monitoramento.repository.NotificacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BuscarNotificacaoService {

    private final NotificacaoRepository notificacaoRepository;

    public List<Notificacao> buscarTodasNotificacoes() {
        return notificacaoRepository.findAllByOrderByDataCriacaoDesc();
    }

    public List<Notificacao> buscarNotificacoesPorStatus(StatusNotificacao status) {
        return notificacaoRepository.findByStatus(status);
    }

    public Optional<Notificacao> buscarNotificacaoAbertaPorAtendimentoETipoDado(Long atendimentoId, TipoDado tipoDado) {
        return notificacaoRepository.findFirstByLeituraSensorAtendimentoIdAndLeituraSensorTipoDadoAndStatusOrderByDataCriacaoDesc(
                atendimentoId, tipoDado, StatusNotificacao.ABERTA);
    }
}
