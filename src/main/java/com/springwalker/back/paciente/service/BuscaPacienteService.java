package com.springwalker.back.paciente.service;

import com.springwalker.back.paciente.dto.PacienteResponseDTO;
import com.springwalker.back.paciente.mapper.PacienteMapper;
import com.springwalker.back.paciente.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BuscaPacienteService {

    private final PacienteRepository pacienteRepository;
    private final PacienteMapper pacienteMapper;

    public List<PacienteResponseDTO> listarTodos() {
        return pacienteRepository.findAll().stream()
                .map(pacienteMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<PacienteResponseDTO> buscarPorId(Long id) {
        return pacienteRepository.findById(id)
                .map(pacienteMapper::toResponseDTO);
    }

    public List<PacienteResponseDTO> buscarPorNome(String nome) {
        return pacienteRepository.findPacientesByNomeContaining(nome).stream()
                .map(pacienteMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<PacienteResponseDTO> buscarPorCpf(String cpf) {
        return pacienteRepository.findByCpf(cpf)
                .map(pacienteMapper::toResponseDTO);
    }
}
