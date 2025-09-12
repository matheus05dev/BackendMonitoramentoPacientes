package com.springwalker.back.atendimento.service;

import com.springwalker.back.atendimento.dto.AtendimentoResponseDTO;
import com.springwalker.back.atendimento.mapper.AtendimentoMapper;
import com.springwalker.back.atendimento.repository.AtendimentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BuscaAtendimentoService {
    private final AtendimentoRepository atendimentoRepository;
    private final AtendimentoMapper atendimentoMapper;

    //lógica de buscar atendimento por id
    public Optional<AtendimentoResponseDTO> buscarAtendimentoPorId(Long id) {
        return atendimentoRepository.findById(id)
                .map(atendimentoMapper::toResponseDTO);
    }

    //lógica de buscar todos atendimento
    public List<AtendimentoResponseDTO> buscarTodosAtendimentos() {
        return atendimentoRepository.findAll()
                .stream()
                .map(atendimentoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
