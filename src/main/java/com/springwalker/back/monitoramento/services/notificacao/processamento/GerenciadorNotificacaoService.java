package com.springwalker.back.monitoramento.services.notificacao.processamento;

import com.springwalker.back.core.enums.Gravidade;
import com.springwalker.back.monitoramento.model.LeituraSensor;
import com.springwalker.back.monitoramento.repository.LeituraSensorRepository;
import com.springwalker.back.monitoramento.services.notificacao.NotificacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GerenciadorNotificacaoService {

    private final LeituraSensorRepository leituraSensorRepository;
    private final NotificacaoService notificacaoService; // O serviço que de fato envia

    // Processa uma leitura de sensor e decide se uma notificação deve ser enviada. A notificação é enviada se a gravidade não for NORMAL.
    public void processarEEnviarNotificacao(LeituraSensor leitura) {
        if (deveNotificar(leitura.getGravidade())) {
            notificacaoService.enviarNotificacao(leitura);
        }
    }


    public List<LeituraSensor> buscarTodasNotificacoes() {
        // Busca todas as leituras e filtra em memória.
        // Para grandes volumes de dados, uma query customizada no repositório seria mais eficiente.
        return leituraSensorRepository.findAll().stream()
                .filter(leitura -> deveNotificar(leitura.getGravidade()))
                .collect(Collectors.toList());
    }

    // Regra de negócio centralizada para determinar se uma gravidade requer notificação
    private boolean deveNotificar(Gravidade gravidade) {
        return gravidade != null && gravidade != Gravidade.NORMAL;
    }
}
