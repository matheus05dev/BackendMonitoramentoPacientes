package com.springwalker.back.funcionario.service;

import com.springwalker.back.atendimento.repository.AtendimentoRepository;
import com.springwalker.back.funcionario.repository.FuncionarioSaudeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeletaFuncionarioSaudeService {

    private final FuncionarioSaudeRepository funcionarioSaudeRepository;
    private final AtendimentoRepository atendimentoRepository;

    @Transactional
    public void execute(Long id) {
        // Verifica se o funcionário existe antes de tentar deletar
        if (!funcionarioSaudeRepository.existsById(id)) {
            throw new RuntimeException("Funcionário não encontrado com ID: " + id);
        }

        // Desvincula o funcionário de todos os atendimentos como médico responsável
        atendimentoRepository.desvincularMedicoResponsavel(id);

        // Desvincula o funcionário de todos os atendimentos como médico de complicação
        atendimentoRepository.desvincularMedicoComplicacao(id);

        // Agora que o funcionário não está mais vinculado a nenhum atendimento, ele pode ser seguramente apagado.
        funcionarioSaudeRepository.deleteById(id);
    }
}
