package com.springwalker.back.monitoramento.service.notificacao.processamento;

import com.springwalker.back.atendimento.model.Atendimento;
import com.springwalker.back.atendimento.repository.AtendimentoRepository;
import com.springwalker.back.core.enums.StatusNotificacao;
import com.springwalker.back.monitoramento.model.LeituraSensor;
import com.springwalker.back.monitoramento.model.Notificacao;
import com.springwalker.back.monitoramento.repository.NotificacaoRepository;
import jakarta.persistence.EntityManager; // Import adicionado
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CriadorNotificacaoService {

    private final NotificacaoRepository notificacaoRepository;
    private final AtendimentoRepository atendimentoRepository;
    private final EntityManager entityManager; // Injetado

    @Transactional
    public Notificacao criarEGravarNotificacao(LeituraSensor leitura) {
        Integer numeroQuarto = null;

        // Busca o Atendimento mais recente diretamente do banco de dados
        if (leitura.getAtendimento() != null && leitura.getAtendimento().getId() != null) {
            // Limpa o cache do EntityManager antes de buscar para garantir que seja carregado fresco
            entityManager.clear();
            Optional<Atendimento> atendimentoOpt = atendimentoRepository.findById(leitura.getAtendimento().getId());
            if (atendimentoOpt.isPresent()) {
                Atendimento atendimentoAtualizado = atendimentoOpt.get();
                if (atendimentoAtualizado.getNumeroQuarto() != null) {
                    numeroQuarto = atendimentoAtualizado.getNumeroQuarto();
                }
            }
        }

        Notificacao notificacao = Notificacao.builder()
                .leituraSensor(leitura)
                .status(StatusNotificacao.ABERTA)
                .dataCriacao(LocalDateTime.now())
                .numeroQuarto(numeroQuarto) // Preenche o numeroQuarto
                .build();
        return notificacaoRepository.save(notificacao);
    }
}
