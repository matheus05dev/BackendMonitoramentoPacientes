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

@Service
@RequiredArgsConstructor
public class CriaPacienteService {

    private final PacienteRepository pacienteRepository;
    private final QuartoRepository quartoRepository;
    private final PacienteMapper pacienteMapper;

    @Transactional
    public PacienteResponseDTO execute(PacienteRequestDTO requestDTO) {
        // Validação de CPF duplicado
        if (pacienteRepository.existsByCpf(requestDTO.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado.");
        }

        // Converte DTO para a entidade Paciente (o campo 'quarto' é ignorado pelo mapper)
        Paciente paciente = pacienteMapper.toEntity(requestDTO);

        // Lógica de negócio para associar o Quarto, se um ID for fornecido
        if (requestDTO.getQuartoId() != null) {
            Quarto quarto = quartoRepository.findById(requestDTO.getQuartoId())
                    .orElseThrow(() -> new RuntimeException("Quarto não encontrado com ID: " + requestDTO.getQuartoId()));
            paciente.setQuarto(quarto);
        }

        // Salva o novo paciente no banco
        Paciente pacienteSalvo = pacienteRepository.save(paciente);

        // Converte a entidade salva para o DTO de resposta e retorna
        return pacienteMapper.toResponseDTO(pacienteSalvo);
    }
}
