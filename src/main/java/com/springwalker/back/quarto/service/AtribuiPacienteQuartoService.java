package com.springwalker.back.quarto.service;

import com.springwalker.back.atendimento.model.Atendimento;
import com.springwalker.back.atendimento.repository.AtendimentoRepository;
import com.springwalker.back.paciente.model.Paciente;
import com.springwalker.back.quarto.model.Quarto;
import com.springwalker.back.paciente.repository.PacienteRepository;
import com.springwalker.back.quarto.repository.QuartoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AtribuiPacienteQuartoService {

    private final PacienteRepository pacienteRepository;
    private final QuartoRepository quartoRepository;
    private final AtendimentoRepository atendimentoRepository; // Injetado

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
            pacienteRepository.save(paciente); // Salva a relação no paciente

            // Agora, atualiza o atendimento em aberto, se existir
            Optional<Atendimento> atendimentoAbertoOpt = atendimentoRepository.findByPacienteIdAndDataSaidaIsNull(pacienteId);
            atendimentoAbertoOpt.ifPresent(atendimento -> {
                atendimento.setQuarto(quarto);
                atendimento.setNumeroQuarto(quarto.getNumero());
                atendimentoRepository.save(atendimento);
                atendimentoRepository.flush(); // Força a sincronização com o banco de dados
            });

            return quarto;
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
}
