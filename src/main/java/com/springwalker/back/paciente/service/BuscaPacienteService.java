package com.springwalker.back.paciente.service;

import com.springwalker.back.paciente.model.Paciente;
import com.springwalker.back.paciente.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuscaPacienteService {

    private final PacienteRepository pacienteRepository;

    // Lógica para buscar todos os pacientes
    public List<Paciente> listarTodos() {
        return pacienteRepository.findAll();
    }

    // Lógica para buscar um paciente por ID
    public Paciente buscarPorId(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Paciente com ID " + id + " não encontrado."));
    }

    // Lógica para buscar pacientes por nome (busca parcial)
    public List<Paciente> buscarPorNome(String nome) {
        return pacienteRepository.findPacientesByNomeContaining(nome);
    }

    // Lógica para buscar pacientes por CPF
    public List<Paciente> buscarPorCpf(String cpf) {
        return pacienteRepository.findPacientesByCpfContaining(cpf);
    }

}
