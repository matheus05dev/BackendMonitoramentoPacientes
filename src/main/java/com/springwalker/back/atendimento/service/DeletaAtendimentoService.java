package com.springwalker.back.atendimento.service;

import com.springwalker.back.atendimento.model.Atendimento;
import com.springwalker.back.atendimento.repository.AtendimentoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class DeletaAtendimentoService {

    private final AtendimentoRepository atendimentoRepository;

    //lógica de apagar atendimento
    @Transactional
    public void deletarAtendimento(Long id) {
        // 1. Busca o atendimento existente pelo ID
        Atendimento atendimentoExistente = atendimentoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Atendimento não encontrado com o ID: " + id));

        // 2. Apaga o atendimento no qual o id coincide
        atendimentoRepository.delete(atendimentoExistente);
    }
}
