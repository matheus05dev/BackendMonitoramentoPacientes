package com.springwalker.back.atendimento.repository;

import com.springwalker.back.atendimento.model.Atendimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface AtendimentoRepository extends JpaRepository<Atendimento, Long> {

    boolean existsByPacienteIdAndDataSaidaIsNull(Long pacienteId);

    Optional<Atendimento> findByPacienteIdAndDataSaidaIsNull(Long pacienteId);

    @Modifying
    @Transactional
    @Query("UPDATE Atendimento a SET a.paciente.id = null WHERE a.paciente.id = ?1")
    void desvincularPaciente(Long pacienteId);


    @Modifying
    @Transactional
    @Query("UPDATE Atendimento a SET a.medicoResponsavel.id = null WHERE a.medicoResponsavel.id = ?1")
    void desvincularMedicoResponsavel(Long medicoId);


    @Modifying
    @Transactional
    @Query("UPDATE Atendimento a SET a.medicoComplicacao.id = null WHERE a.medicoComplicacao.id = ?1")
    void desvincularMedicoComplicacao(Long medicoId);
}
