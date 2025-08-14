package com.springwalker.back.service.funcionarioSaude;

import com.springwalker.back.repository.FuncionarioSaudeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DelataFuncionarioSaudeService {

    private final FuncionarioSaudeRepository funcionarioSaudeRepository;

    @Transactional
    // Lógica para deletar um funcionário por ID
    public void deletar(Long id) {
        funcionarioSaudeRepository.deleteById(id);
    }
}
