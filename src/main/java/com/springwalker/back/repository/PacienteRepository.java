package com.springwalker.back.repository;

import com.springwalker.back.model.Paciente;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    List<Paciente> findByNomeContainingIgnoreCase(String nome);

    Paciente findByEmail(String email);

    Paciente findByCpf(String cpf);

    Paciente findByNome(String nome);

    List<Paciente> findPacientesByNomeContaining(String nome);



    List<Paciente> findPacientesByNomeContainingOrCpfContaining(String nome, String cpf);

    List<Paciente> nome(@NotEmpty(message = "O nome deve ser informado") String nome);

    List<Paciente> findPacientesByCpfContaining(String cpf);

    String cpf(@NotEmpty(message = "O CPF deve ser informado") String cpf);

    @Query("Select nome From Paciente where cpf = ?1 or nome = ?1")
    List<Paciente> buscarPacientePorNomeOuCpf(String texto);
}
