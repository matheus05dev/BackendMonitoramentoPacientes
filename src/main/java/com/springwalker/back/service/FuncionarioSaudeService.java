package com.springwalker.back.service;

import com.springwalker.back.model.FuncionarioSaude;
import com.springwalker.back.repository.FuncionarioSaudeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FuncionarioSaudeService {

    @Autowired
    private FuncionarioSaudeRepository funcionarioSaudeRepository;

    // Lógica para buscar todos os funcionários
    public List<FuncionarioSaude> listarTodos() {
        return funcionarioSaudeRepository.findAll();
    }

    // Lógica para salvar um funcionário novo ou existente
    public FuncionarioSaude salvar(FuncionarioSaude funcionarioSaude) {
        return funcionarioSaudeRepository.save(funcionarioSaude);
    }

    // Lógica para salvar uma lista de funcionários
    public List<FuncionarioSaude> salvarTodos(List<FuncionarioSaude> funcionarios) {
        return funcionarioSaudeRepository.saveAll(funcionarios);
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

    // Lógica para atualizar um funcionário existente por ID, alterando apenas os campos não nulos
    public FuncionarioSaude atualizar(Long id, FuncionarioSaude funcionarioSaude) {
        FuncionarioSaude funcionarioSaudeExistente = funcionarioSaudeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));

        if(funcionarioSaude.getCpf() != null){
            funcionarioSaudeExistente.setCpf(funcionarioSaude.getCpf());
        }

        if(funcionarioSaude.getEmail() != null){
            funcionarioSaudeExistente.setEmail(funcionarioSaude.getEmail());
        }

        if(funcionarioSaude.getNome() != null){
            funcionarioSaudeExistente.setNome(funcionarioSaude.getNome());
        }

        if(funcionarioSaude.getDataNascimento() != null){
            funcionarioSaudeExistente.setDataNascimento(funcionarioSaude.getDataNascimento());
        }

        if(funcionarioSaude.getTelefones() != null){
            funcionarioSaudeExistente.setTelefones(funcionarioSaude.getTelefones());
        }

        if (funcionarioSaude.getCargo() != null) {
            funcionarioSaudeExistente.setCargo(funcionarioSaude.getCargo());
        }

        if (funcionarioSaude.getEspecialidades() != null) {
            funcionarioSaudeExistente.setEspecialidades(funcionarioSaude.getEspecialidades());
        }

        if (funcionarioSaude.getIdentificacao() != null) {
            funcionarioSaudeExistente.setIdentificacao(funcionarioSaude.getIdentificacao());
        }

        return funcionarioSaudeRepository.save(funcionarioSaudeExistente);
    }

    // Lógica para deletar um funcionário por ID
    public void deletar(Long id) {
        funcionarioSaudeRepository.deleteById(id);
    }
}