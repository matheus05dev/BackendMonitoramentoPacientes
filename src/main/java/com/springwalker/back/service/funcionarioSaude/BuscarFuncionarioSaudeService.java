package com.springwalker.back.service.funcionarioSaude;

import com.springwalker.back.model.FuncionarioSaude;
import com.springwalker.back.repository.FuncionarioSaudeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BuscarFuncionarioSaudeService {

    private final FuncionarioSaudeRepository funcionarioSaudeRepository;

    // Lógica para buscar todos os funcionários
    public List<FuncionarioSaude> listarTodos() {
        return funcionarioSaudeRepository.findAll();
    }

    // Lógica para buscar um funcionário por ID
    public Optional<FuncionarioSaude> buscarPorId(Long id) {
        return funcionarioSaudeRepository.findById(id);
    }

    // Lógica para buscar funcionários por nome (busca parcial)
    public List<FuncionarioSaude> buscarPorNome(String nome) {
        return funcionarioSaudeRepository.findFuncionarioSaudesByNomeContaining(nome);
    }

    // Lógica para buscar um funcionário por CPF
    public FuncionarioSaude buscarPorCpf(String cpf) {
        return funcionarioSaudeRepository.findFuncionarioSaudeByCpf(cpf);
    }
}
