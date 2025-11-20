package com.springwalker.back.quarto.model;

import com.springwalker.back.paciente.model.Paciente;
import com.springwalker.back.quarto.enums.LocalizacaoQuarto;
import com.springwalker.back.quarto.enums.TipoQuarto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuartoTest {

    private Quarto quarto;
    private Paciente paciente1;
    private Paciente paciente2;

    @BeforeEach
    void setUp() {
        quarto = Quarto.builder()
                .numero(101)
                .localizacao(LocalizacaoQuarto.SETOR_CENTRO)
                .tipo(TipoQuarto.ENFERMARIA)
                .capacidade(2) // Aumentar a capacidade para testar a adição de paciente existente
                .build();
        
        paciente1 = new Paciente();
        paciente1.setId(1L);
        paciente1.setNome("João Silva");

        paciente2 = new Paciente();
        paciente2.setId(2L);
        paciente2.setNome("Maria Souza");
    }

    @Test
    @DisplayName("Deve adicionar um paciente com sucesso")
    void shouldAddPacienteSuccessfully() {
        assertTrue(quarto.temVaga());
        assertEquals(0, quarto.getPacientes().size());

        quarto.adicionarPaciente(paciente1);

        assertTrue(quarto.temVaga());
        assertEquals(1, quarto.getPacientes().size());
        assertTrue(quarto.getPacientes().contains(paciente1));
    }

    @Test
    @DisplayName("Deve falhar ao adicionar paciente em quarto lotado")
    void shouldFailToAddPacienteWhenRoomIsFull() {
        quarto.setCapacidade(1); // Reduzir a capacidade para este teste
        quarto.adicionarPaciente(paciente1);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            quarto.adicionarPaciente(paciente2);
        });

        assertEquals("Quarto 101 já atingiu a capacidade máxima de 1 pacientes.", exception.getMessage());
        assertEquals(1, quarto.getPacientes().size());
    }

    @Test
    @DisplayName("Deve falhar ao adicionar um paciente que já está no quarto")
    void shouldFailToAddExistingPaciente() {
        quarto.adicionarPaciente(paciente1);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            quarto.adicionarPaciente(paciente1);
        });

        assertEquals("Paciente já está neste quarto.", exception.getMessage());
        assertEquals(1, quarto.getPacientes().size());
    }

    @Test
    @DisplayName("Deve remover um paciente com sucesso")
    void shouldRemovePacienteSuccessfully() {
        quarto.adicionarPaciente(paciente1);
        assertEquals(1, quarto.getPacientes().size());

        quarto.removerPaciente(paciente1);

        assertTrue(quarto.temVaga());
        assertEquals(0, quarto.getPacientes().size());
        assertFalse(quarto.getPacientes().contains(paciente1));
    }

    @Test
    @DisplayName("Deve falhar ao remover um paciente que não está no quarto")
    void shouldFailToRemoveNonExistingPaciente() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            quarto.removerPaciente(paciente1);
        });

        assertEquals("Paciente não encontrado no quarto 101", exception.getMessage());
    }

    @Test
    @DisplayName("Deve retornar true quando houver vaga")
    void shouldReturnTrueWhenThereIsVacancy() {
        assertTrue(quarto.temVaga());
    }

    @Test
    @DisplayName("Deve retornar false quando não houver vaga")
    void shouldReturnFalseWhenThereIsNoVacancy() {
        quarto.setCapacidade(1); // Ajustar capacidade para o teste
        quarto.adicionarPaciente(paciente1);
        assertFalse(quarto.temVaga());
    }
}
