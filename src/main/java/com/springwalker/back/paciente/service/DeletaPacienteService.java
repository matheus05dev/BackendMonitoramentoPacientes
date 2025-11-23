package com.springwalker.back.paciente.service;

import com.springwalker.back.atendimento.repository.AtendimentoRepository;
import com.springwalker.back.paciente.repository.PacienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.NoSuchElementException;


@Service
@RequiredArgsConstructor
public class DeletaPacienteService {

    private final PacienteRepository pacienteRepository;
    private final AtendimentoRepository atendimentoRepository;

    @Transactional
    public void execute(Long id) {
        // Adiciona verificação de existência para fornecer um erro claro
        if (!pacienteRepository.existsById(id)) {
            throw new NoSuchElementException("Paciente não encontrado com ID: " + id);
        }

        // Passo 1: Desvincula o paciente de todos os seus atendimentos
        atendimentoRepository.desvincularPaciente(id);

        // Passo 2: Agora que a dependência foi removida, exclui o paciente com segurança
        pacienteRepository.deleteById(id);
    }
}
