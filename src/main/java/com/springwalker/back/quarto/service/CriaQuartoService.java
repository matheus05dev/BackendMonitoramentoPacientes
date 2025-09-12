package com.springwalker.back.quarto.service;

import com.springwalker.back.quarto.dto.QuartoRequestDTO;
import com.springwalker.back.quarto.dto.QuartoResponseDTO;
import com.springwalker.back.quarto.mapper.QuartoMapper;
import com.springwalker.back.quarto.model.Quarto;
import com.springwalker.back.quarto.repository.QuartoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CriaQuartoService {

    private final QuartoRepository quartoRepository;
    private final QuartoMapper quartoMapper;

    @Transactional
    // Lógica para inserir vários quartos
    public List<QuartoResponseDTO> inserirVarios(List<QuartoRequestDTO> dtos) {
        List<Quarto> quartos = dtos.stream().map(quartoMapper::toEntity).toList();
        List<Quarto> salvos = quartoRepository.saveAll(quartos);
        return salvos.stream().map(quartoMapper::toResponseDTO).toList();
    }

    @Transactional
    // Lógica para inserir um novo quarto
    public QuartoResponseDTO inserir(QuartoRequestDTO dto) {
        Quarto quarto = quartoMapper.toEntity(dto);
        Quarto salvo = quartoRepository.save(quarto);
        return quartoMapper.toResponseDTO(salvo);
    }
}
