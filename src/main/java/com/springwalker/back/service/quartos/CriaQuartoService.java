package com.springwalker.back.service.quartos;

import com.springwalker.back.model.Quarto;
import com.springwalker.back.repository.QuartoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CriaQuartoService {

    private final QuartoRepository quartoRepository;

    @Transactional
    // Lógica para inserir vários quartos
    public List<Quarto> inserirVarios(List<Quarto> quartos) {
        return quartoRepository.saveAll(quartos);
    }

    @Transactional
    // Lógica para inserir um novo quarto
    public Quarto inserir(Quarto quarto) {
        return quartoRepository.save(quarto);
    }
}
