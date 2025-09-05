package com.springwalker.back.funcionario.repository;

import com.springwalker.back.funcionario.model.FuncionarioSaude;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FuncionarioSaudeRepository extends JpaRepository<FuncionarioSaude, Long> {

    Optional<FuncionarioSaude> findFuncionarioSaudeByCpf(String cpf);

    boolean existsByCpf(String cpf);

    List<FuncionarioSaude> findFuncionarioSaudesByNomeContaining(String nome);
}
