package com.springwalker.back.service.paciente;

import com.springwalker.back.model.Paciente;
import com.springwalker.back.repository.PacienteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CriaPacienteService {

    private final PacienteRepository pacienteRepository;

    @Transactional
    // Lógica de negócio para inserir um novo paciente
    public Paciente inserir(Paciente paciente) {
        // Valida se já existe um paciente com o mesmo CPF para evitar duplicidade
        if (pacienteRepository.findByCpf(paciente.getCpf()) != null) {
            throw new IllegalArgumentException("CPF já cadastrado.");
        }
        return pacienteRepository.save(paciente);
    }

    @Transactional
    // Lógica para inserir vários pacientes de uma vez
    public List<Paciente> inserirVarios(List<Paciente> pacientes) {
        return pacienteRepository.saveAll(pacientes);
    }

}
