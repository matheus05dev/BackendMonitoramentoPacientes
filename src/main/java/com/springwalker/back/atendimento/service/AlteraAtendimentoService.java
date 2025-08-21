package com.springwalker.back.atendimento.service;

import com.springwalker.back.core.enums.Cargo;
import com.springwalker.back.core.enums.StatusPaciente;
import com.springwalker.back.atendimento.model.Atendimento;
import com.springwalker.back.funcionario.model.FuncionarioSaude;
import com.springwalker.back.atendimento.repository.AtendimentoRepository;
import com.springwalker.back.funcionario.repository.FuncionarioSaudeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AlteraAtendimentoService {


    private final AtendimentoRepository atendimentoRepository;

    private  final FuncionarioSaudeRepository funcionarioSaudeRepository;


    //lógica de alterar atendimento
    @Transactional
    public Atendimento alterarAtendimento(Long id, Atendimento atendimentoAtualizado) {
        // 1. Busca o atendimento existente pelo ID
        Atendimento atendimentoExistente = atendimentoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Atendimento não encontrado com o ID: " + id));

        // 2. Atualiza o status do paciente
        atendimentoExistente.setStatusPaciente(atendimentoAtualizado.getStatusPaciente());

        // 3. Adiciona a lógica para a data de saída baseada no status do paciente
        if (atendimentoAtualizado.getStatusPaciente() == StatusPaciente.Liberado) {
            // Se o status for "Liberado", define a data de saída para a data e hora atuais.
            atendimentoExistente.setDataSaida(LocalDateTime.now());
        } else if (atendimentoAtualizado.getStatusPaciente() == StatusPaciente.Internado) {
            // Se o status for alterado para "Internado", limpa a data de saída (caso tenha sido liberado e re-internado).
            atendimentoExistente.setDataSaida(null);
        }

        // 4. Atualiza os demais campos do atendimento com os dados do objeto recebido
        atendimentoExistente.setAcompanhante(atendimentoAtualizado.getAcompanhante());
        atendimentoExistente.setCondicoesPreexistentes(atendimentoAtualizado.getCondicoesPreexistentes());
        atendimentoExistente.setObservacoes(atendimentoAtualizado.getObservacoes());
        atendimentoExistente.setTratamento(atendimentoAtualizado.getTratamento());
        atendimentoExistente.setDiagnostico(atendimentoAtualizado.getDiagnostico());

        // 5. Lógica para complicação
        if (atendimentoAtualizado.getDiagnostico_complicacao() != null) {
            atendimentoExistente.setDiagnostico_complicacao(atendimentoAtualizado.getDiagnostico_complicacao());
            atendimentoExistente.setTratamento_complicacao(atendimentoAtualizado.getTratamento_complicacao());

            if (atendimentoAtualizado.getMedicoComplicacao() != null && atendimentoAtualizado.getMedicoComplicacao().getId() != null) {
                FuncionarioSaude medicoComplicacao = funcionarioSaudeRepository.findById(atendimentoAtualizado.getMedicoComplicacao().getId())
                        .orElseThrow(() -> new NoSuchElementException("Médico de complicação não encontrado com o ID: " + atendimentoAtualizado.getMedicoComplicacao().getId()));

                if (medicoComplicacao.getCargo() != Cargo.MEDICO) {
                    throw new IllegalArgumentException("O funcionário de complicação deve ser um médico.");
                }
                atendimentoExistente.setMedicoComplicacao(medicoComplicacao);
            }
        } else {
            // Se a complicação for removida, limpa os campos relacionados
            atendimentoExistente.setDiagnostico_complicacao(null);
            atendimentoExistente.setTratamento_complicacao(null);
            atendimentoExistente.setMedicoComplicacao(null);
        }

        // 6. Salva e retorna o atendimento atualizado
        return atendimentoRepository.save(atendimentoExistente);
    }
}
