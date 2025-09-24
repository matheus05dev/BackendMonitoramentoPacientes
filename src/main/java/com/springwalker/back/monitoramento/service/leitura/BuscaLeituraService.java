package com.springwalker.back.monitoramento.service.leitura;

import com.springwalker.back.monitoramento.dto.leitura.LeituraSensorResponseDTO;
import com.springwalker.back.monitoramento.mapper.LeituraSensorMapper;
import com.springwalker.back.monitoramento.repository.LeituraSensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BuscaLeituraService {
    private final LeituraSensorRepository leituraSensorRepository;
    private final LeituraSensorMapper leituraSensorMapper;

    public List<LeituraSensorResponseDTO> buscarPorAtendimento(Long atendimentoId) {
        return leituraSensorRepository.findByAtendimentoId(atendimentoId).stream()
                .map(leituraSensorMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<LeituraSensorResponseDTO> buscarTodas() {
        return leituraSensorRepository.findAll().stream()
                .map(leituraSensorMapper::toResponse)
                .collect(Collectors.toList());
    }

}
