package com.springwalker.back.quarto.service;

import com.springwalker.back.quarto.repository.QuartoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeletaQuartoService {

    private final QuartoRepository quartoRepository;

    @Transactional
    // Lógica para apagar um quarto
    public void excluir(Long id) {
        if (!quartoRepository.existsById(id)) {
            throw new IllegalStateException("Quarto não encontrado com o ID: " + id);
        }
        quartoRepository.deleteById(id);
    }

}
