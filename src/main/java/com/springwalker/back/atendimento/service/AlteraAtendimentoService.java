package com.springwalker.back.atendimento.service;

import com.springwalker.back.atendimento.dto.AtendimentoRequestDTO;
import com.springwalker.back.atendimento.dto.AtendimentoResponseDTO;
import com.springwalker.back.atendimento.mapper.AtendimentoMapper;
import com.springwalker.back.atendimento.model.Atendimento;
import com.springwalker.back.funcionario.model.FuncionarioSaude;
import com.springwalker.back.atendimento.repository.AtendimentoRepository;
import com.springwalker.back.funcionario.repository.FuncionarioSaudeRepository;
import com.springwalker.back.paciente.model.Paciente;
import com.springwalker.back.paciente.repository.PacienteRepository;
import com.springwalker.back.quarto.model.Quarto;
import com.springwalker.back.quarto.repository.QuartoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AlteraAtendimentoService {
    private final AtendimentoRepository atendimentoRepository;
    private final FuncionarioSaudeRepository funcionarioSaudeRepository;
    private final PacienteRepository pacienteRepository;
    private final QuartoRepository quartoRepository;
    private final AtendimentoMapper atendimentoMapper;

    @Transactional
    public AtendimentoResponseDTO alterarAtendimento(Long id, AtendimentoRequestDTO dto) {
        Atendimento atendimentoExistente = atendimentoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Atendimento não encontrado com o ID: " + id));

        // Regra de negócio: não permitir alteração se já estiver Liberado
        if (atendimentoExistente.getStatusPaciente() != null && atendimentoExistente.getStatusPaciente().name().equals("Liberado")) {
            throw new IllegalStateException("Não é permitido alterar um atendimento já liberado.");
        }

        // Atualiza campos do atendimento
        atendimentoExistente.setStatusPaciente(dto.getStatusPaciente());
        if (dto.getStatusPaciente() != null && dto.getStatusPaciente().name().equals("Liberado")) {
            atendimentoExistente.setDataSaida(LocalDateTime.now());
        } else if (dto.getStatusPaciente() != null && dto.getStatusPaciente().name().equals("Internado")) {
            atendimentoExistente.setDataSaida(null);
        }
        atendimentoExistente.setAcompanhante(dto.getAcompanhante());
        atendimentoExistente.setCondicoesPreexistentes(dto.getCondicoesPreexistentes());
        atendimentoExistente.setObservacoes(dto.getObservacoes());
        atendimentoExistente.setTratamento(dto.getTratamento());
        atendimentoExistente.setDiagnostico(dto.getDiagnostico());
        atendimentoExistente.setDiagnostico_complicacao(dto.getDiagnosticoComplicacao());
        atendimentoExistente.setTratamento_complicacao(dto.getTratamentoComplicacao());
        atendimentoExistente.setStatusMonitoramento(dto.getStatusMonitoramento());

        // Atualiza o quarto se um novo quartoId for fornecido
        if (dto.getQuartoId() != null) {
            Quarto novoQuarto = quartoRepository.findById(dto.getQuartoId())
                    .orElseThrow(() -> new NoSuchElementException("Quarto não encontrado"));
            atendimentoExistente.setQuarto(novoQuarto);
            atendimentoExistente.setNumeroQuarto(novoQuarto.getNumero());
        } else {
            atendimentoExistente.setQuarto(null);
            atendimentoExistente.setNumeroQuarto(null);
        }

        // Atualiza paciente se um novo pacienteId for fornecido
        if (dto.getPacienteId() != null) {
            Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                    .orElseThrow(() -> new NoSuchElementException("Paciente não encontrado"));
            atendimentoExistente.setPaciente(paciente);
            atendimentoExistente.setNomePaciente(paciente.getNome());
        }

        // Atualiza médicos
        if (dto.getMedicoResponsavelId() != null) {
            FuncionarioSaude medicoResponsavel = funcionarioSaudeRepository.findById(dto.getMedicoResponsavelId())
                    .orElseThrow(() -> new NoSuchElementException("Médico responsável não encontrado"));
            atendimentoExistente.setMedicoResponsavel(medicoResponsavel);
            atendimentoExistente.setNomeMedicoResponsavel(medicoResponsavel.getNome());
        }
        if (dto.getMedicoComplicacaoId() != null) {
            FuncionarioSaude medicoComplicacao = funcionarioSaudeRepository.findById(dto.getMedicoComplicacaoId())
                    .orElseThrow(() -> new NoSuchElementException("Médico complicação não encontrado"));
            atendimentoExistente.setMedicoComplicacao(medicoComplicacao);
            atendimentoExistente.setNomeMedicoComplicacao(medicoComplicacao.getNome());
        } else {
            atendimentoExistente.setMedicoComplicacao(null);
            atendimentoExistente.setNomeMedicoComplicacao(null);
        }

        Atendimento salvo = atendimentoRepository.save(atendimentoExistente);
        return atendimentoMapper.toResponseDTO(salvo);
    }
}
