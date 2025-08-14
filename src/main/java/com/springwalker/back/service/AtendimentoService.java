package com.springwalker.back.service;

import com.springwalker.back.enums.Cargo;
import com.springwalker.back.enums.StatusPaciente;
import com.springwalker.back.model.Atendimento;
import com.springwalker.back.model.FuncionarioSaude;
import com.springwalker.back.repository.AtendimentoRepository;
import com.springwalker.back.repository.FuncionarioSaudeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AtendimentoService {


    private final AtendimentoRepository atendimentoRepository;

    private  final FuncionarioSaudeRepository funcionarioSaudeRepository;

    //lógica de criar atendimento
    @Transactional
    public Atendimento criarAtendimento(Atendimento atendimento) {
        // Valida se o médico responsável existe
        FuncionarioSaude medicoResponsavel = funcionarioSaudeRepository.findById(atendimento.getMedicoResponsavel().getId())
                .orElseThrow(() -> new NoSuchElementException("Médico responsável não encontrado com o ID: " + atendimento.getMedicoResponsavel().getId()));

        // Valida se o cargo do funcionário é MEDICO
        if (medicoResponsavel.getCargo() != Cargo.MEDICO) {
            throw new IllegalArgumentException("O funcionário responsável deve ser um médico.");
        }

        // Se houver um médico de complicação, valida se ele também é um médico.
        if (atendimento.getMedicoComplicacao() != null) {
            FuncionarioSaude medicoComplicacao = funcionarioSaudeRepository.findById(atendimento.getMedicoComplicacao().getId())
                    .orElseThrow(() -> new NoSuchElementException("Médico de complicação não encontrado com o ID: " + atendimento.getMedicoComplicacao().getId()));

            if (medicoComplicacao.getCargo() != Cargo.MEDICO) {
                throw new IllegalArgumentException("O funcionário de complicação deve ser um médico.");
            }
        }

        return atendimentoRepository.save(atendimento);
    }
    //lógica de buscar atendimento por id
    public Optional<Atendimento> buscarAtendimentoPorId(Long id) {
        return atendimentoRepository.findById(id);
    }

    //lógica de buscar todos atendimento
    public List<Atendimento> buscarTodosAtendimentos() {
        return atendimentoRepository.findAll();
    }

    //lógica de apagar atendimento
    @Transactional
    public Atendimento deletarAtendimento(Long id, Atendimento atendimento) {
        // 1. Busca o atendimento existente pelo ID
        Atendimento atendimentoExistente = atendimentoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Atendimento não encontrado com o ID: " + id));

        // 2. Apaga o atendimento no qual o id coincide
        atendimentoRepository.delete(atendimentoExistente);
        return atendimentoExistente;
    }

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