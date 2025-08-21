package com.springwalker.back.atendimento.repository;

import com.springwalker.back.atendimento.model.Atendimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AtendimentoRepository extends JpaRepository<Atendimento, Long> {


    boolean existsByPacienteIdAndDataSaidaIsNull(Long pacienteId);
}
