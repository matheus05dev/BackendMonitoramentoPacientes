package com.springwalker.back.service.paciente;

import com.springwalker.back.model.Paciente;
import com.springwalker.back.repository.PacienteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlteraPacienteService {

    private PacienteRepository pacienteRepository;

    // Lógica de negócio para alterar um paciente

    @Transactional
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
}
