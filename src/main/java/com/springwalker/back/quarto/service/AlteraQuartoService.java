package com.springwalker.back.quarto.service;

import com.springwalker.back.quarto.dto.QuartoRequestDTO;
import com.springwalker.back.quarto.dto.QuartoResponseDTO;
import com.springwalker.back.quarto.mapper.QuartoMapper;
import com.springwalker.back.quarto.model.Quarto;
import com.springwalker.back.quarto.repository.QuartoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlteraQuartoService {

    private final QuartoRepository quartoRepository;
    private final QuartoMapper quartoMapper;

    @Transactional
    public QuartoResponseDTO alterar(Long id, QuartoRequestDTO dto) {
        Quarto quartoExistente = quartoRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Quarto não encontrado com o ID: " + id));
        // Atualiza os campos da entidade existente com base no DTO
        quartoMapper.updateFromDto(dto, quartoExistente);
        Quarto salvo = quartoRepository.save(quartoExistente);
        return quartoMapper.toResponseDTO(salvo);
    }
}
