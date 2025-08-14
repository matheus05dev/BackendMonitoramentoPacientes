package com.springwalker.back.service.quartos;

import com.springwalker.back.model.Paciente;
import com.springwalker.back.model.Quarto;
import com.springwalker.back.repository.PacienteRepository;
import com.springwalker.back.repository.QuartoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AtribuiPacienteQuartoService {

    private final PacienteRepository pacienteRepository;
    private final QuartoRepository quartoRepository;

    // Lógica de negócio para alocar paciente em um quarto
    @Transactional // Garante que as operações em paciente e quarto ocorram em uma única transação
    public Quarto alocarPaciente(Long quartoId, Long pacienteId) {
        Quarto quarto = quartoRepository.findById(quartoId)
                .orElseThrow(() -> new IllegalStateException("Quarto não encontrado com o ID: " + quartoId));
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new IllegalStateException("Paciente não encontrado com o ID: " + pacienteId));

        try {
            quarto.adicionarPaciente(paciente);
            paciente.setQuarto(quarto);
            pacienteRepository.save(paciente);
            return quartoRepository.save(quarto);
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
}
