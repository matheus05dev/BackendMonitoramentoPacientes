package com.springwalker.back.repository;

import com.springwalker.back.model.FuncionarioSaude;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FuncionarioSaudeRepository extends JpaRepository<FuncionarioSaude, Long> {

    List<FuncionarioSaude> findByNomeContainingIgnoreCase(String nome);

    FuncionarioSaude findByEmail(String email);

    FuncionarioSaude findByCpf(String cpf);

    FuncionarioSaude findByNome(String nome);

    List<FuncionarioSaude> findFuncionarioSaudesByNomeContaining(String nome);

    List<FuncionarioSaude> findFuncionarioSaudesByNomeContainingOrCpfContaining(String nome, String cpf);
}
