package com.springwalker.back.repository;

import com.springwalker.back.model.FuncionarioSaude;
import com.springwalker.back.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FuncionarioSaudeRepository extends JpaRepository<FuncionarioSaude, Long> {

    List<FuncionarioSaude> findByNomeContainingIgnoreCase(String nome);

    FuncionarioSaude findByEmail(String email);

    FuncionarioSaude findByCpf(String cpf);

    FuncionarioSaude findByNome(String nome);

    List<FuncionarioSaude> findFuncionarioSaudesByNomeContaining(String nome);

    List<FuncionarioSaude> findFuncionarioSaudesByNomeContainingOrCpfContaining(String nome, String cpf);

    FuncionarioSaude findFuncionarioSaudeByCpf(String cpf);

//    @Query("Select nome From FuncionarioSaude where cpf = ?1 or nome = ?1")
//    List<FuncionarioSaude> buscarFuncionarioSaudePorNomeOuCpf(String texto);
}
