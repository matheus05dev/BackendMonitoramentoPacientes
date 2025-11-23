package com.springwalker.back.paciente.service;

import com.springwalker.back.paciente.dto.PacienteRequestDTO;
import com.springwalker.back.paciente.dto.PacienteResponseDTO;
import com.springwalker.back.paciente.mapper.PacienteMapper;
import com.springwalker.back.paciente.model.Paciente;
import com.springwalker.back.paciente.repository.PacienteRepository;
import com.springwalker.back.quarto.model.Quarto;
import com.springwalker.back.quarto.repository.QuartoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AlteraPacienteService {

    private final PacienteRepository pacienteRepository;
    private final QuartoRepository quartoRepository;
    private final PacienteMapper pacienteMapper;

    @Transactional
    public PacienteResponseDTO execute(Long id, PacienteRequestDTO requestDTO) {
        // Busca a entidade existente ou lança uma exceção
        Paciente pacienteExistente = pacienteRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Paciente não encontrado com ID: " + id));

        // Usa o mapper para atualizar os campos da entidade com base no DTO
        pacienteMapper.updateFromDto(requestDTO, pacienteExistente);

        // Lógica de negócio para atualizar o quarto
        handleQuartoUpdate(requestDTO, pacienteExistente);

        // Salva a entidade atualizada
        Paciente pacienteAtualizado = pacienteRepository.save(pacienteExistente);

        // Retorna o DTO de resposta
        return pacienteMapper.toResponseDTO(pacienteAtualizado);
    }

    private void handleQuartoUpdate(PacienteRequestDTO dto, Paciente paciente) {
        Long novoQuartoId = dto.getQuartoId();
        Long quartoAtualId = (paciente.getQuarto() != null) ? paciente.getQuarto().getId() : null;

        // Se o ID do quarto não mudou, não faz nada
        if (Objects.equals(novoQuartoId, quartoAtualId)) {
            return;
        }

        // Se o novo ID for nulo, desassocia o paciente do quarto
        if (novoQuartoId == null) {
            paciente.setQuarto(null);
        } else {
            // Se for um novo ID, busca o novo quarto e associa ao paciente
            Quarto novoQuarto = quartoRepository.findById(novoQuartoId)
                    .orElseThrow(() -> new NoSuchElementException("Quarto não encontrado com ID: " + novoQuartoId)); // Alterado para NoSuchElementException
            paciente.setQuarto(novoQuarto);
        }
    }
}
