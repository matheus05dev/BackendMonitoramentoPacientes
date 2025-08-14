package com.springwalker.back.repository;

import com.springwalker.back.model.Atendimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AtendimentoRepository extends JpaRepository<Atendimento, Long> {


    boolean existsByPacienteIdAndDataSaidaIsNull(Long pacienteId);
}
