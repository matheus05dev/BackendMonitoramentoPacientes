package com.springwalker.back.atendimento.service;

import com.springwalker.back.atendimento.model.Atendimento;
import com.springwalker.back.atendimento.repository.AtendimentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
