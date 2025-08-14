package com.springwalker.back.service.atendimento;

import com.springwalker.back.model.Atendimento;
import com.springwalker.back.repository.AtendimentoRepository;
import com.springwalker.back.repository.FuncionarioSaudeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class BuscaAtendimentoService {

    private final AtendimentoRepository atendimentoRepository;

    //lógica de buscar atendimento por id
    public Optional<Atendimento> buscarAtendimentoPorId(Long id) {
        return atendimentoRepository.findById(id);
    }

    //lógica de buscar todos atendimento
    public List<Atendimento> buscarTodosAtendimentos() {
        return atendimentoRepository.findAll();
    }
}
