package com.springwalker.back.service.atendimento;

import com.springwalker.back.enums.Cargo;
import com.springwalker.back.enums.StatusPaciente;
import com.springwalker.back.model.Atendimento;
import com.springwalker.back.model.FuncionarioSaude;
import com.springwalker.back.repository.AtendimentoRepository;
import com.springwalker.back.repository.FuncionarioSaudeRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor

public class CriaAtendimentoService {

    private final FuncionarioSaudeRepository funcionarioSaudeRepository;
    private final AtendimentoRepository atendimentoRepository;


    //lógica de criar atendimento
    @Transactional
    public Atendimento criarAtendimento(@Valid Atendimento atendimento) {
        if (atendimento.getPaciente() == null || atendimento.getPaciente().getId() == null) {
            throw new IllegalArgumentException("O paciente deve ser informado para criar um atendimento.");
        }

        if (atendimentoRepository.existsByPacienteIdAndDataSaidaIsNull(atendimento.getPaciente().getId())) {
            throw new IllegalStateException("O paciente já possui um atendimento em aberto.");
        }

        FuncionarioSaude medicoResponsavel = funcionarioSaudeRepository.findById(atendimento.getMedicoResponsavel().getId())
                .orElseThrow(() -> new NoSuchElementException("Médico responsável não encontrado com o ID: " + atendimento.getMedicoResponsavel().getId()));

        if (medicoResponsavel.getCargo() != Cargo.MEDICO) {
            throw new IllegalArgumentException("O funcionário responsável deve ser um médico.");
        }

        if (atendimento.getMedicoComplicacao() != null && atendimento.getMedicoComplicacao().getId() != null) {
            FuncionarioSaude medicoComplicacao = funcionarioSaudeRepository.findById(atendimento.getMedicoComplicacao().getId())
                    .orElseThrow(() -> new NoSuchElementException("Médico de complicação não encontrado com o ID: " + atendimento.getMedicoComplicacao().getId()));

            if (medicoComplicacao.getCargo() != Cargo.MEDICO) {
                throw new IllegalArgumentException("O funcionário de complicação deve ser um médico.");
            }
        }

        atendimento.setDataEntrada(LocalDateTime.now());
        atendimento.setStatusPaciente(StatusPaciente.Internado);
        atendimento.setDataSaida(null);

        return atendimentoRepository.save(atendimento);
    }
}