package com.springwalker.back.funcionario.service;

import com.springwalker.back.funcionario.dto.FuncionarioSaudeRequestDTO;
import com.springwalker.back.funcionario.dto.FuncionarioSaudeResponseDTO;
import com.springwalker.back.funcionario.mapper.FuncionarioMapper;
import com.springwalker.back.funcionario.model.FuncionarioSaude;
import com.springwalker.back.funcionario.repository.FuncionarioSaudeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlteraFuncionarioSaudeService {

    private final FuncionarioSaudeRepository funcionarioSaudeRepository;
    private final FuncionarioMapper funcionarioMapper;

    @Transactional
    public FuncionarioSaudeResponseDTO execute(Long id, FuncionarioSaudeRequestDTO dto) {
        // Busca a entidade existente
        FuncionarioSaude funcionarioExistente = funcionarioSaudeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado com ID: " + id));

        // Usa o mapper para atualizar a entidade com os dados do DTO
        funcionarioMapper.updateFromDto(dto, funcionarioExistente);

        // Salva a entidade atualizada
        FuncionarioSaude funcionarioAtualizado = funcionarioSaudeRepository.save(funcionarioExistente);

        // Retorna o DTO de resposta
        return funcionarioMapper.toResponseDTO(funcionarioAtualizado);
    }
}
