package com.springwalker.back.atendimento.service;

import com.springwalker.back.atendimento.dto.AtendimentoResponseDTO;
import com.springwalker.back.atendimento.mapper.AtendimentoMapper;
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
    private final AtendimentoMapper atendimentoMapper;

    // lógica de apagar atendimento
    @Transactional
    public AtendimentoResponseDTO deletarAtendimento(Long id) {
        Atendimento atendimentoExistente = atendimentoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Atendimento não encontrado com o ID: " + id));
        atendimentoRepository.delete(atendimentoExistente);
        return atendimentoMapper.toResponseDTO(atendimentoExistente);
    }
}
