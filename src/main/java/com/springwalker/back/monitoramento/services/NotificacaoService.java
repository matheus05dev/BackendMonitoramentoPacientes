package com.springwalker.back.monitoramento.services;

import com.springwalker.back.atendimento.repository.AtendimentoRepository;
import com.springwalker.back.monitoramento.mapper.LeituraSensorMapper;
import com.springwalker.back.monitoramento.repository.LeituraSensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificacaoService {
    private final LeituraSensorRepository leituraSensorRepository;
    private final AtendimentoRepository atendimentoRepository;
    private final LeituraSensorMapper leituraSensorMapper;


}
