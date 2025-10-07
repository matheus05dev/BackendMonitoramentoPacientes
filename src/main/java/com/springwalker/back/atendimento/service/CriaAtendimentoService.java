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
    private final PacienteRepository pacienteRepository;
    private final QuartoRepository quartoRepository;
    private final AtendimentoMapper atendimentoMapper;

    @Transactional
    public AtendimentoResponseDTO criarAtendimento(@Valid AtendimentoRequestDTO dto) {
        Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                .orElseThrow(() -> new NoSuchElementException("Paciente não encontrado"));
        if (atendimentoRepository.existsByPacienteIdAndDataSaidaIsNull(dto.getPacienteId())) {
            throw new IllegalStateException("O paciente já possui um atendimento em aberto.");
        }

        FuncionarioSaude medicoResponsavel = funcionarioSaudeRepository.findById(dto.getMedicoResponsavelId())
                .orElseThrow(() -> new NoSuchElementException("Médico responsável não encontrado"));
        if (medicoResponsavel.getCargo() != com.springwalker.back.core.enums.Cargo.MEDICO) {
            throw new IllegalArgumentException("O funcionário responsável deve ser um médico.");
        }

        FuncionarioSaude medicoComplicacao = null;
        if (dto.getMedicoComplicacaoId() != null) {
            medicoComplicacao = funcionarioSaudeRepository.findById(dto.getMedicoComplicacaoId())
                    .orElseThrow(() -> new NoSuchElementException("Médico de complicação não encontrado"));
            if (medicoComplicacao.getCargo() != com.springwalker.back.core.enums.Cargo.MEDICO) {
                throw new IllegalArgumentException("O funcionário de complicação deve ser um médico.");
            }
        }

        Atendimento atendimento = atendimentoMapper.toEntity(dto);
        atendimento.setPaciente(paciente);
        atendimento.setMedicoResponsavel(medicoResponsavel);
        atendimento.setMedicoComplicacao(medicoComplicacao);
        atendimento.setDataEntrada(LocalDateTime.now());
        atendimento.setStatusPaciente(com.springwalker.back.core.enums.StatusPaciente.Internado);
        atendimento.setDataSaida(null);
        atendimento.setStatusMonitoramento(dto.getStatusMonitoramento());

        // Associa o quarto e o número do quarto APENAS se o quartoId for fornecido
        if (dto.getQuartoId() != null) {
            Quarto quarto = quartoRepository.findById(dto.getQuartoId())
                    .orElseThrow(() -> new NoSuchElementException("Quarto não encontrado"));
            atendimento.setQuarto(quarto);
            atendimento.setNumeroQuarto(quarto.getNumero());
        }

        // Preenchendo nomes imutáveis
        atendimento.setNomePaciente(paciente.getNome());
        atendimento.setNomeMedicoResponsavel(medicoResponsavel.getNome());
        atendimento.setNomeMedicoComplicacao(medicoComplicacao != null ? medicoComplicacao.getNome() : null);

        Atendimento salvo = atendimentoRepository.save(atendimento);
        return atendimentoMapper.toResponseDTO(salvo);
    }
}
