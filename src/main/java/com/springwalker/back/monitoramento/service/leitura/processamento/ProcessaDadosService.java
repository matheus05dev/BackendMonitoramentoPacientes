package com.springwalker.back.monitoramento.service.leitura.processamento;

import com.springwalker.back.atendimento.model.Atendimento;
import com.springwalker.back.atendimento.repository.AtendimentoRepository;
import com.springwalker.back.atendimento.enums.StatusMonitoramento;
import com.springwalker.back.monitoramento.dto.leitura.LeituraSensorRequestDTO;
import com.springwalker.back.monitoramento.dto.leitura.LeituraSensorResponseDTO;
import com.springwalker.back.monitoramento.enums.leitura.TipoDado;
import com.springwalker.back.monitoramento.mapper.LeituraSensorMapper;
import com.springwalker.back.monitoramento.model.LeituraSensor;
import com.springwalker.back.monitoramento.repository.LeituraSensorRepository;
import com.springwalker.back.monitoramento.service.notificacao.processamento.GerenciadorNotificacaoService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProcessaDadosService {

    private final LeituraSensorRepository leituraSensorRepository;
    private final AtendimentoRepository atendimentoRepository;
    private final LeituraSensorMapper leituraSensorMapper;
    private final AnaliseDadosSensorService analiseDadosSensorService;
    private final GerenciadorNotificacaoService gerenciadorNotificacaoService;
    private final EntityManager entityManager;

    @Transactional
    public LeituraSensorResponseDTO salvar(Long atendimentoId, LeituraSensorRequestDTO requestDTO) {
        // Limpa o cache do EntityManager para garantir que o Atendimento seja carregado fresco do banco de dados
        entityManager.clear();

        Atendimento atendimento = atendimentoRepository.findById(atendimentoId)
                .orElseThrow(() -> new RuntimeException("Atendimento não encontrado com o ID: " + atendimentoId));

        if (atendimento.getStatusMonitoramento() != StatusMonitoramento.MONITORANDO) {
            throw new RuntimeException("O atendimento com ID " + atendimentoId + " não está em modo de monitoramento.");
        }

        LeituraSensor leituraSensor = leituraSensorMapper.toModel(requestDTO);
        leituraSensor.setDataHora(LocalDateTime.now());
        leituraSensor.setAtendimento(atendimento);

        analiseDadosSensorService.analisarDadosSensor(leituraSensor);

        LeituraSensor savedLeitura = leituraSensorRepository.save(leituraSensor);

        if (savedLeitura.getTipoDado() != TipoDado.MEDICACAO) {
            gerenciadorNotificacaoService.processarEEnviarNotificacao(savedLeitura);
        }

        return leituraSensorMapper.toResponse(savedLeitura);
    }
}
