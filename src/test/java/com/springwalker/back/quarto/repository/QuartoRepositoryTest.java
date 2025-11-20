package com.springwalker.back.quarto.repository;

import com.springwalker.back.quarto.enums.LocalizacaoQuarto;
import com.springwalker.back.quarto.enums.TipoQuarto;
import com.springwalker.back.quarto.model.Quarto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class QuartoRepositoryTest {

    @Autowired
    private QuartoRepository quartoRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Deve persistir um quarto com sucesso")
    void shouldPersistQuartoSuccessfully() {
        Quarto quarto = Quarto.builder()
                .numero(101)
                .localizacao(LocalizacaoQuarto.SETOR_NORTE)
                .tipo(TipoQuarto.AMBULATORIO)
                .capacidade(1)
                .build();

        Quarto savedQuarto = quartoRepository.save(quarto);

        assertNotNull(savedQuarto.getId());
        assertEquals(101, savedQuarto.getNumero());
        assertEquals(LocalizacaoQuarto.SETOR_NORTE, savedQuarto.getLocalizacao());
        assertEquals(TipoQuarto.AMBULATORIO, savedQuarto.getTipo());
        assertEquals(1, savedQuarto.getCapacidade());

        Quarto foundQuarto = entityManager.find(Quarto.class, savedQuarto.getId());
        assertNotNull(foundQuarto);
        assertEquals(savedQuarto.getId(), foundQuarto.getId());
    }

    @Test
    @DisplayName("Deve encontrar um quarto por ID")
    void shouldFindQuartoById() {
        Quarto quarto = Quarto.builder()
                .numero(202)
                .localizacao(LocalizacaoQuarto.SETOR_SUL)
                .tipo(TipoQuarto.ENFERMARIA)
                .capacidade(2)
                .build();
        Quarto persistedQuarto = entityManager.persistAndFlush(quarto);

        Optional<Quarto> foundQuarto = quartoRepository.findById(persistedQuarto.getId());

        assertTrue(foundQuarto.isPresent());
        assertEquals(persistedQuarto.getId(), foundQuarto.get().getId());
    }

    @Test
    @DisplayName("Deve retornar vazio quando o quarto não for encontrado por ID")
    void shouldReturnEmptyWhenQuartoNotFoundById() {
        Optional<Quarto> foundQuarto = quartoRepository.findById(999L);
        assertFalse(foundQuarto.isPresent());
    }

    @Test
    @DisplayName("Deve deletar um quarto por ID")
    void shouldDeleteQuartoById() {
        Quarto quarto = Quarto.builder()
                .numero(303)
                .localizacao(LocalizacaoQuarto.SETOR_LESTE)
                .tipo(TipoQuarto.ENFERMARIA)
                .capacidade(4)
                .build();
        Quarto persistedQuarto = entityManager.persistAndFlush(quarto);

        quartoRepository.deleteById(persistedQuarto.getId());

        Quarto deletedQuarto = entityManager.find(Quarto.class, persistedQuarto.getId());
        assertNull(deletedQuarto);
    }

    @Test
    @DisplayName("Deve atualizar um quarto com sucesso")
    void shouldUpdateQuartoSuccessfully() {
        Quarto quarto = Quarto.builder()
                .numero(404)
                .localizacao(LocalizacaoQuarto.SETOR_OESTE)
                .tipo(TipoQuarto.UTI)
                .capacidade(1)
                .build();
        Quarto persistedQuarto = entityManager.persistAndFlush(quarto);

        persistedQuarto.setNumero(405);
        persistedQuarto.setTipo(TipoQuarto.ISOLAMENTO);
        Quarto updatedQuarto = quartoRepository.save(persistedQuarto);

        assertEquals(persistedQuarto.getId(), updatedQuarto.getId());
        assertEquals(405, updatedQuarto.getNumero());
        assertEquals(TipoQuarto.ISOLAMENTO, updatedQuarto.getTipo());
    }
}
