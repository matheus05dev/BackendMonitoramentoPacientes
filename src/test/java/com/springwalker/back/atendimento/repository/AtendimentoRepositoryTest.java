package com.springwalker.back.atendimento.repository;

import com.springwalker.back.atendimento.enums.Diagnostico;
import com.springwalker.back.atendimento.model.Atendimento;
import com.springwalker.back.paciente.model.Paciente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class AtendimentoRepositoryTest {

    @Autowired
    private AtendimentoRepository atendimentoRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Paciente paciente;

    @Test
    @DisplayName("Deve persistir um atendimento com sucesso")
    void shouldPersistAtendimentoSuccessfully() {
        paciente = entityManager.persist(new Paciente());
        Atendimento atendimento = Atendimento.builder()
                .paciente(paciente)
                .diagnostico(Diagnostico.S02)
                .tratamento("Gesso")
                .dataEntrada(LocalDateTime.now())
                .build();

        Atendimento savedAtendimento = atendimentoRepository.save(atendimento);

        assertNotNull(savedAtendimento.getId());
    }

    @Test
    @DisplayName("Deve encontrar um atendimento por ID")
    void shouldFindAtendimentoById() {
        paciente = entityManager.persist(new Paciente());
        Atendimento atendimento = Atendimento.builder()
                .paciente(paciente)
                .diagnostico(Diagnostico.J12)
                .tratamento("Antibiótico")
                .dataEntrada(LocalDateTime.now())
                .build();
        Atendimento persistedAtendimento = entityManager.persistAndFlush(atendimento);

        Optional<Atendimento> foundAtendimento = atendimentoRepository.findById(persistedAtendimento.getId());

        assertTrue(foundAtendimento.isPresent());
        assertEquals(persistedAtendimento.getId(), foundAtendimento.get().getId());
    }

    @Test
    @DisplayName("Deve verificar se existe atendimento em aberto para o paciente")
    void shouldCheckIfOpenAtendimentoExistsForPaciente() {
        paciente = entityManager.persist(new Paciente());
        Atendimento atendimento = Atendimento.builder()
                .paciente(paciente)
                .diagnostico(Diagnostico.R55)
                .tratamento("Controle de pressão")
                .dataEntrada(LocalDateTime.now())
                .build();
        entityManager.persistAndFlush(atendimento);

        boolean exists = atendimentoRepository.existsByPacienteIdAndDataSaidaIsNull(paciente.getId());
        assertTrue(exists);
    }

    @Test
    @DisplayName("Deve encontrar atendimento em aberto por ID do paciente")
    void shouldFindOpenAtendimentoByPacienteId() {
        paciente = entityManager.persist(new Paciente());
        Atendimento atendimento = Atendimento.builder()
                .paciente(paciente)
                .diagnostico(Diagnostico.E86)
                .tratamento("Insulina")
                .dataEntrada(LocalDateTime.now())
                .build();
        entityManager.persistAndFlush(atendimento);

        Optional<Atendimento> foundAtendimento = atendimentoRepository.findByPacienteIdAndDataSaidaIsNull(paciente.getId());
        assertTrue(foundAtendimento.isPresent());
    }

    @Test
    @DisplayName("Deve desvincular paciente do atendimento")
    void shouldUnlinkPacienteFromAtendimento() {
        paciente = entityManager.persist(new Paciente());
        Atendimento atendimento = Atendimento.builder()
                .paciente(paciente)
                .diagnostico(Diagnostico.J22)
                .tratamento("Inalador")
                .dataEntrada(LocalDateTime.now())
                .build();
        entityManager.persistAndFlush(atendimento);

        atendimentoRepository.desvincularPaciente(paciente.getId());
        entityManager.clear(); // Limpa o cache para forçar a recarga do banco

        Atendimento updatedAtendimento = entityManager.find(Atendimento.class, atendimento.getId());
        assertNull(updatedAtendimento.getPaciente());
    }
}
