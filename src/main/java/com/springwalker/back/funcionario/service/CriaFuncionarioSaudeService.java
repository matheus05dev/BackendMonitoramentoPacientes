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
public class CriaFuncionarioSaudeService {

    private final FuncionarioSaudeRepository funcionarioSaudeRepository;
    private final FuncionarioMapper funcionarioMapper;

    @Transactional
    public FuncionarioSaudeResponseDTO execute(FuncionarioSaudeRequestDTO requestDTO) {
        // Validação de CPF duplicado
        if (funcionarioSaudeRepository.existsByCpf(requestDTO.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado.");
        }

        // Converte DTO de requisição para a Entidade
        FuncionarioSaude funcionario = funcionarioMapper.toEntity(requestDTO);

        // Salva a entidade no banco de dados
        FuncionarioSaude funcionarioSalvo = funcionarioSaudeRepository.save(funcionario);

        // Converte a entidade salva para o DTO de resposta e retorna
        return funcionarioMapper.toResponseDTO(funcionarioSalvo);
    }
}
