package com.springwalker.back.paciente.repository;

import com.springwalker.back.paciente.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    Optional<Paciente> findByCpf(String cpf);

    boolean existsByCpf(String cpf);

    List<Paciente> findPacientesByNomeContaining(String nome);

    @Query("SELECT p FROM Paciente p LEFT JOIN FETCH p.quarto WHERE p.id = :id")
    Optional<Paciente> findByIdWithQuarto(@Param("id") Long id);

}
