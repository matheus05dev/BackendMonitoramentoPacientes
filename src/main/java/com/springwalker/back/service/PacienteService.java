package com.springwalker.back.service;

import com.springwalker.back.model.Paciente;
import com.springwalker.back.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    // Lógica para buscar todos os pacientes
    public List<Paciente> listarTodos() {
        return pacienteRepository.findAll();
    }

    // Lógica de negócio para inserir um novo paciente
    public Paciente inserir(Paciente paciente) {
        // Valida se já existe um paciente com o mesmo CPF para evitar duplicidade
        if (pacienteRepository.findByCpf(paciente.getCpf()) != null) {
            throw new IllegalArgumentException("CPF já cadastrado.");
        }
        return pacienteRepository.save(paciente);
    }

    // Lógica de negócio para alterar um paciente
    public Paciente alterar(Long id, Paciente pacienteAtualizado) {
        Paciente pacienteExistente = pacienteRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Paciente com ID " + id + " não encontrado."));

        // Atualiza apenas os campos que não são nulos na requisição
        if (pacienteAtualizado.getCpf() != null) {
            pacienteExistente.setCpf(pacienteAtualizado.getCpf());
        }
        if (pacienteAtualizado.getEmail() != null) {
            pacienteExistente.setEmail(pacienteAtualizado.getEmail());
        }
        if (pacienteAtualizado.getNome() != null) {
            pacienteExistente.setNome(pacienteAtualizado.getNome());
        }
        if (pacienteAtualizado.getDataNascimento() != null) {
            pacienteExistente.setDataNascimento(pacienteAtualizado.getDataNascimento());
        }
        if (pacienteAtualizado.getTelefones() != null) {
            pacienteExistente.setTelefones(pacienteAtualizado.getTelefones());
        }
        if (pacienteAtualizado.getAlergias() != null) {
            pacienteExistente.setAlergias(pacienteAtualizado.getAlergias());
        }

        return pacienteRepository.save(pacienteExistente);
    }

    // Lógica para excluir um paciente por ID
    public void excluir(Long id) {
        if (!pacienteRepository.existsById(id)) {
            throw new IllegalStateException("Paciente com ID " + id + " não encontrado.");
        }
        pacienteRepository.deleteById(id);
    }

    // Lógica para inserir vários pacientes de uma vez
    public List<Paciente> inserirVarios(List<Paciente> pacientes) {
        return pacienteRepository.saveAll(pacientes);
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