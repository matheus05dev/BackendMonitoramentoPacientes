package com.springwalker.back.service.funcionarioSaude;

import com.springwalker.back.model.FuncionarioSaude;
import com.springwalker.back.repository.FuncionarioSaudeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor

public class CriaFuncionarioSaudeService {


    private final FuncionarioSaudeRepository funcionarioSaudeRepository;

    @Transactional
    // Lógica para salvar um funcionário novo ou existente
    public FuncionarioSaude salvar(FuncionarioSaude funcionarioSaude) {
        return funcionarioSaudeRepository.save(funcionarioSaude);
    }

    @Transactional
    // Lógica para salvar uma lista de funcionários
    public List<FuncionarioSaude> salvarTodos(List<FuncionarioSaude> funcionarios) {
        return funcionarioSaudeRepository.saveAll(funcionarios);
    }
}