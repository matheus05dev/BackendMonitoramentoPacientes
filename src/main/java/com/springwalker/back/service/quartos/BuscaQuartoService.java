package com.springwalker.back.service.quartos;

import com.springwalker.back.model.Quarto;
import com.springwalker.back.repository.QuartoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuscaQuartoService {

    private final QuartoRepository quartoRepository;

    // Lógica para buscar todos os quartos
    public List<Quarto> listarTodos() {
        return quartoRepository.findAll();
    }

    // Lógica para buscar um quarto por ID
    public Quarto buscarPorId(Long id) {
        return quartoRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Quarto não encontrado com o ID: " + id));
    }

}
