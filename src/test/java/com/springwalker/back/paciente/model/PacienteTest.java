package com.springwalker.back.paciente.model;

import com.springwalker.back.core.enums.LocalizacaoQuarto;
import com.springwalker.back.core.enums.TipoQuarto;
import com.springwalker.back.core.model.Telefone;
import com.springwalker.back.quarto.model.Quarto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PacienteTest {

    private Quarto quarto;
    private Telefone telefone;

    @BeforeEach
    void setUp() {
        quarto = Quarto.builder()
                .id(1L)
                .numero(101)
                .localizacao(LocalizacaoQuarto.SETOR_SUL)
                .tipo(TipoQuarto.ISOLAMENTO)
                .capacidade(1)
                .build();

        telefone = new Telefone(1L, 11, "99999-8888");
    }

    @Test
    @DisplayName("Deve criar um Paciente com o construtor vazio")
    void shouldCreatePacienteWithNoArgsConstructor() {
        Paciente paciente = new Paciente();
        assertNotNull(paciente);
        assertNull(paciente.getId()); // Assuming ID is null before persistence
    }

    @Test
    @DisplayName("Deve criar um Paciente com o construtor completo")
    void shouldCreatePacienteWithAllArgsConstructor() {
        List<String> alergias = Arrays.asList("Pólen", "Amendoim");
        Paciente paciente = new Paciente(alergias, quarto);

        assertNotNull(paciente);
        assertEquals(alergias, paciente.getAlergias());
        assertEquals(quarto, paciente.getQuarto());
    }

    @Test
    @DisplayName("Deve definir e obter alergias")
    void shouldSetAndGetAlergias() {
        Paciente paciente = new Paciente();
        List<String> alergias = Arrays.asList("Lactose", "Glúten");
        paciente.setAlergias(alergias);

        assertEquals(alergias, paciente.getAlergias());
    }

    @Test
    @DisplayName("Deve definir e obter quarto")
    void shouldSetAndGetQuarto() {
        Paciente paciente = new Paciente();
        paciente.setQuarto(quarto);

        assertEquals(quarto, paciente.getQuarto());
    }

    @Test
    @DisplayName("Deve definir e obter ID (herdado de Pessoa)")
    void shouldSetAndGetIdFromPessoa() {
        Paciente paciente = new Paciente();
        paciente.setId(1L);
        assertEquals(1L, paciente.getId());
    }

    @Test
    @DisplayName("Deve definir e obter nome (herdado de Pessoa)")
    void shouldSetAndGetNameFromPessoa() {
        Paciente paciente = new Paciente();
        paciente.setNome("João Silva");
        assertEquals("João Silva", paciente.getNome());
    }

    @Test
    @DisplayName("Deve definir e obter CPF (herdado de Pessoa)")
    void shouldSetAndGetCpfFromPessoa() {
        Paciente paciente = new Paciente();
        paciente.setCpf("123.456.789-00");
        assertEquals("123.456.789-00", paciente.getCpf());
    }

    @Test
    @DisplayName("Deve definir e obter data de nascimento (herdado de Pessoa)")
    void shouldSetAndGetDataNascimentoFromPessoa() {
        Paciente paciente = new Paciente();
        LocalDate dataNascimento = LocalDate.of(1990, 5, 15);
        paciente.setDataNascimento(dataNascimento);
        assertEquals(dataNascimento, paciente.getDataNascimento());
    }

    @Test
    @DisplayName("Deve definir e obter telefones (herdado de Pessoa)")
    void shouldSetAndGetTelefonesFromPessoa() {
        Paciente paciente = new Paciente();
        List<Telefone> telefones = Collections.singletonList(telefone);
        paciente.setTelefones(telefones);

        assertNotNull(paciente.getTelefones());
        assertFalse(paciente.getTelefones().isEmpty());
        assertEquals(1, paciente.getTelefones().size());
        assertEquals(telefone, paciente.getTelefones().get(0));
    }

    @Test
    @DisplayName("Deve definir e obter email (herdado de Pessoa)")
    void shouldSetAndGetEmailFromPessoa() {
        Paciente paciente = new Paciente();
        paciente.setEmail("joao.silva@example.com");
        assertEquals("joao.silva@example.com", paciente.getEmail());
    }


    @Test
    @DisplayName("Deve gerar toString corretamente")
    void shouldGenerateToStringCorrectly() {
        List<String> alergias = Arrays.asList("Pólen");
        Paciente paciente = Paciente.builder()
                .id(1L)
                .nome("Maria")
                .cpf("111.222.333-44")
                .dataNascimento(LocalDate.of(1985, 10, 20))
                .telefones(Collections.singletonList(telefone))
                .email("maria@example.com")
                .alergias(alergias)
                .quarto(quarto)
                .build();

        String pacienteToString = paciente.toString();

        // Verificações para os campos próprios de Paciente
        assertTrue(pacienteToString.contains("alergias=[Pólen]"));
        assertTrue(pacienteToString.contains("quarto=" + quarto.toString())); // Verifica o toString completo do Quarto

        // Verificações para os campos de Pessoa (via super.toString())
        assertTrue(pacienteToString.contains("super=Pessoa("));
        assertTrue(pacienteToString.contains("id=1"));
        assertTrue(pacienteToString.contains("nome=Maria"));
        assertTrue(pacienteToString.contains("cpf=111.222.333-44"));
        assertTrue(pacienteToString.contains("dataNascimento=1985-10-20"));
        assertTrue(pacienteToString.contains("email=maria@example.com"));

        // Verificações para Telefone dentro do toString() de Pessoa
        String expectedTelefoneToString = telefone.toString();
        assertTrue(pacienteToString.contains("telefones=[" + expectedTelefoneToString + "]"));
    }

    @Test
    @DisplayName("Dois Pacientes com o mesmo ID devem ser iguais")
    void shouldBeEqualIfIdsAreSame() {
        Paciente paciente1 = Paciente.builder().id(1L).nome("Teste").build();
        Paciente paciente2 = Paciente.builder().id(1L).nome("Outro Teste").build();

        assertEquals(paciente1, paciente2);
        assertEquals(paciente1.hashCode(), paciente2.hashCode());
    }

    @Test
    @DisplayName("Dois Pacientes com IDs diferentes não devem ser iguais")
    void shouldNotBeEqualIfIdsAreDifferent() {
        Paciente paciente1 = Paciente.builder().id(1L).nome("Teste").build();
        Paciente paciente2 = Paciente.builder().id(2L).nome("Teste").build();

        assertNotEquals(paciente1, paciente2);
        assertNotEquals(paciente1.hashCode(), paciente2.hashCode());
    }

    @Test
    @DisplayName("Paciente deve ser igual a si mesmo")
    void shouldBeEqualToItself() {
        Paciente paciente = Paciente.builder().id(1L).build();
        assertEquals(paciente, paciente);
    }

    @Test
    @DisplayName("Paciente não deve ser igual a null")
    void shouldNotBeEqualToNull() {
        Paciente paciente = Paciente.builder().id(1L).build();
        assertNotEquals(null, paciente);
    }

    @Test
    @DisplayName("Paciente não deve ser igual a outro tipo de objeto")
    void shouldNotBeEqualToDifferentTypeOfObject() {
        Paciente paciente = Paciente.builder().id(1L).build();
        assertNotEquals(paciente, new Object());
    }
}
