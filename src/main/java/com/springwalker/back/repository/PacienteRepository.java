package com.springwalker.back.repository;

import com.springwalker.back.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    List<Paciente> findByNomeContainingIgnoreCase(String nome);

    Paciente findByEmail(String email);

    Paciente findByCpf(String cpf);

    Paciente findByNome(String nome);

    List<Paciente> findPacientesByNomeContaining(String nome);



    List<Paciente> findPacientesByNomeContainingOrCpfContaining(String nome, String cpf);

}
