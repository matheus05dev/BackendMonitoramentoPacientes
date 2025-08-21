package com.springwalker.back.quarto.service;

import com.springwalker.back.paciente.model.Paciente;
import com.springwalker.back.quarto.model.Quarto;
import com.springwalker.back.paciente.repository.PacienteRepository;
import com.springwalker.back.quarto.repository.QuartoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RemovePacienteQuartoService {

    private final PacienteRepository pacienteRepository;
    private final QuartoRepository quartoRepository;

    // Lógica de negócio para remover paciente de um quarto
    @Transactional
    public Quarto removerPaciente(Long quartoId, Long pacienteId) {
        Quarto quarto = quartoRepository.findById(quartoId)
                .orElseThrow(() -> new IllegalStateException("Quarto não encontrado com o ID: " + quartoId));
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new IllegalStateException("Paciente não encontrado com o ID: " + pacienteId));

        try {
            quarto.removerPaciente(paciente);
            paciente.setQuarto(null);
            pacienteRepository.save(paciente);
            return quartoRepository.save(quarto);
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
}
