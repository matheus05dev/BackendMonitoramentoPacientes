package com.springwalker.back.service.funcionarioSaude;

import com.springwalker.back.model.FuncionarioSaude;
import com.springwalker.back.repository.FuncionarioSaudeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlteraFuncionarioSaudeService {

    private final FuncionarioSaudeRepository funcionarioSaudeRepository;

    @Transactional
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
}
