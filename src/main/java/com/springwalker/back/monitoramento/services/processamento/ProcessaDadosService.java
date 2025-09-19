package com.springwalker.back.monitoramento.services.processamento;

import com.springwalker.back.atendimento.model.Atendimento;
import com.springwalker.back.atendimento.repository.AtendimentoRepository;
import com.springwalker.back.core.enums.StatusMonitoramento;
import com.springwalker.back.monitoramento.dto.LeituraSensorRequestDTO;
import com.springwalker.back.monitoramento.dto.LeituraSensorResponseDTO;
import com.springwalker.back.monitoramento.mapper.LeituraSensorMapper;
import com.springwalker.back.monitoramento.model.LeituraSensor;
import com.springwalker.back.monitoramento.repository.LeituraSensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProcessaDadosService {

    private final LeituraSensorRepository leituraSensorRepository;
    private final AtendimentoRepository atendimentoRepository;
    private final LeituraSensorMapper leituraSensorMapper;
    private final AnaliseDadosSensorService analiseDadosSensorService;

    public LeituraSensorResponseDTO salvar(Long atendimentoId, LeituraSensorRequestDTO requestDTO) {
        Atendimento atendimento = atendimentoRepository.findById(atendimentoId)
                .orElseThrow(() -> new RuntimeException("Atendimento não encontrado com o ID: " + atendimentoId));

        if (atendimento.getStatusMonitoramento() != StatusMonitoramento.MONITORANDO) {
            throw new RuntimeException("O atendimento com ID " + atendimentoId + " não está em modo de monitoramento.");
        }

        LeituraSensor leituraSensor = leituraSensorMapper.toModel(requestDTO);
        leituraSensor.setDataHora(LocalDateTime.now());
        leituraSensor.setAtendimento(atendimento);

        // 1. Analisar os dados e popular a entidade ANTES de salvar
        analiseDadosSensorService.analisarDadosSensor(leituraSensor);

        // 2. Salvar a entidade já com os dados da análise
        LeituraSensor savedLeitura = leituraSensorRepository.save(leituraSensor);

        LeituraSensorResponseDTO responseDTO = leituraSensorMapper.toResponse(savedLeitura);

        return responseDTO;
    }
}
