package com.springwalker.back.funcionario.service;

import com.springwalker.back.funcionario.dto.FuncionarioSaudeResponseDTO;
import com.springwalker.back.funcionario.mapper.FuncionarioSaudeMapper;
import com.springwalker.back.funcionario.repository.FuncionarioSaudeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BuscarFuncionarioSaudeService {

    private final FuncionarioSaudeRepository funcionarioSaudeRepository;
    private final FuncionarioSaudeMapper funcionarioSaudeMapper;

    public List<FuncionarioSaudeResponseDTO> listarTodos() {
        return funcionarioSaudeRepository.findAll()
                .stream()
                .map(funcionarioSaudeMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<FuncionarioSaudeResponseDTO> buscarPorId(Long id) {
        return funcionarioSaudeRepository.findById(id)
                .map(funcionarioSaudeMapper::toResponseDTO);
    }

    public List<FuncionarioSaudeResponseDTO> buscarPorNome(String nome) {
        return funcionarioSaudeRepository.findFuncionarioSaudesByNomeContaining(nome)
                .stream()
                .map(funcionarioSaudeMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<FuncionarioSaudeResponseDTO> buscarPorCpf(String cpf) {
        // CORREÇÃO: Removido Optional.ofNullable, pois o repositório já retorna Optional
        return funcionarioSaudeRepository.findFuncionarioSaudeByCpf(cpf)
                .map(funcionarioSaudeMapper::toResponseDTO);
    }
}
