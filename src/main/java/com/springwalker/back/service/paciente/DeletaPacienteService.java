package com.springwalker.back.service.paciente;

import com.springwalker.back.repository.PacienteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeletaPacienteService {

    private final PacienteRepository pacienteRepository;

    // Lógica para excluir um paciente por ID
    @Transactional
    public void excluir(Long id) {
        if (!pacienteRepository.existsById(id)) {
            throw new IllegalStateException("Paciente com ID " + id + " não encontrado.");
        }
        pacienteRepository.deleteById(id);
    }
}
