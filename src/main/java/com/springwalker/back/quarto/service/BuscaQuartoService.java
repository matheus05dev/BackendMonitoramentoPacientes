package com.springwalker.back.quarto.service;

import com.springwalker.back.quarto.dto.QuartoResponseDTO;
import com.springwalker.back.quarto.mapper.QuartoMapper;
import com.springwalker.back.quarto.repository.QuartoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuscaQuartoService {

    private final QuartoRepository quartoRepository;
    private final QuartoMapper quartoMapper;

    // Lógica para buscar todos os quartos
    public List<QuartoResponseDTO> listarTodos() {
        return quartoRepository.findAll().stream().map(quartoMapper::toResponseDTO).toList();
    }

    // Lógica para buscar um quarto por ID
    public QuartoResponseDTO buscarPorId(Long id) {
        return quartoRepository.findById(id)
                .map(quartoMapper::toResponseDTO)
                .orElseThrow(() -> new IllegalStateException("Quarto não encontrado com o ID: " + id));
    }

}
