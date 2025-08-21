package com.springwalker.back.quarto.service;

import com.springwalker.back.quarto.model.Quarto;
import com.springwalker.back.quarto.repository.QuartoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlteraQuartoService {

    private final QuartoRepository quartoRepository;

    @Transactional
    // Lógica para alterar um quarto
    public Quarto alterar(Long id, Quarto quarto) {
        Quarto quartoExistente = quartoRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Quarto não encontrado com o ID: " + id));

        if (quarto.getCapacidade() != null) {
            quartoExistente.setCapacidade(quarto.getCapacidade());
        }
        if (quarto.getLocalizacao() != null) {
            quartoExistente.setLocalizacao(quarto.getLocalizacao());
        }
        if (quarto.getNumero() != null) {
            quartoExistente.setNumero(quarto.getNumero());
        }
        if (quarto.getTipo() != null) {
            quartoExistente.setTipo(quarto.getTipo());
        }

        return quartoRepository.save(quartoExistente);
    }
}
