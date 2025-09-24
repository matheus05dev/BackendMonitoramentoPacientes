package com.springwalker.back.funcionario.model;

import com.springwalker.back.atendimento.model.Atendimento;
import com.springwalker.back.core.enums.Cargo;
import com.springwalker.back.core.enums.Sexo;
import com.springwalker.back.core.model.Telefone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FuncionarioSaudeTest {

    private Telefone telefone;
    private Atendimento atendimentoMock;

    @BeforeEach
    void setUp() {
        telefone = new Telefone(1L, 21, "98765-4321");
        // CORREÇÃO: Inicializando atendimentoMock usando o builder
        atendimentoMock = Atendimento.builder().id(10L).build();
    }

    @Test
    @DisplayName("Deve criar um FuncionarioSaude com o construtor vazio")
    void shouldCreateFuncionarioSaudeWithNoArgsConstructor() {
        FuncionarioSaude funcionario = new FuncionarioSaude();
        assertNotNull(funcionario);
        assertNull(funcionario.getId());
    }

    @Test
    @DisplayName("Deve criar um FuncionarioSaude com o construtor completo")
    void shouldCreateFuncionarioSaudeWithAllArgsConstructor() {
        List<String> especialidades = Arrays.asList("Cardiologia", "Pediatria");
        List<Atendimento> atendimentos = Collections.singletonList(atendimentoMock);
        FuncionarioSaude funcionario = new FuncionarioSaude(Cargo.MEDICO, especialidades, "ID123", atendimentos, atendimentos);

        assertNotNull(funcionario);
        assertEquals(Cargo.MEDICO, funcionario.getCargo());
        assertEquals(especialidades, funcionario.getEspecialidades());
        assertEquals("ID123", funcionario.getIdentificacao());
        assertEquals(atendimentos, funcionario.getAtendimentos());
        assertEquals(atendimentos, funcionario.getAtendimentosComplicacao());
    }

    @Test
    @DisplayName("Deve definir e obter cargo")
    void shouldSetAndGetCargo() {
        FuncionarioSaude funcionario = new FuncionarioSaude();
        funcionario.setCargo(Cargo.ENFERMEIRO);
        assertEquals(Cargo.ENFERMEIRO, funcionario.getCargo());
    }

    @Test
    @DisplayName("Deve definir e obter especialidades")
    void shouldSetAndGetEspecialidades() {
        FuncionarioSaude funcionario = new FuncionarioSaude();
        List<String> especialidades = Arrays.asList("Cirurgia", "Anestesiologia");
        funcionario.setEspecialidades(especialidades);
        assertEquals(especialidades, funcionario.getEspecialidades());
    }

    @Test
    @DisplayName("Deve definir e obter identificacao")
    void shouldSetAndGetIdentificacao() {
        FuncionarioSaude funcionario = new FuncionarioSaude();
        funcionario.setIdentificacao("ID456");
        assertEquals("ID456", funcionario.getIdentificacao());
    }

    @Test
    @DisplayName("Deve definir e obter atendimentos")
    void shouldSetAndGetAtendimentos() {
        FuncionarioSaude funcionario = new FuncionarioSaude();
        List<Atendimento> atendimentos = Collections.singletonList(atendimentoMock);
        funcionario.setAtendimentos(atendimentos);
        assertEquals(atendimentos, funcionario.getAtendimentos());
    }

    @Test
    @DisplayName("Deve definir e obter atendimentosComplicacao")
    void shouldSetAndGetAtendimentosComplicacao() {
        FuncionarioSaude funcionario = new FuncionarioSaude();
        List<Atendimento> atendimentos = Collections.singletonList(atendimentoMock);
        funcionario.setAtendimentosComplicacao(atendimentos);
        assertEquals(atendimentos, funcionario.getAtendimentosComplicacao());
    }

    // Testes para campos herdados de Pessoa
    @Test
    @DisplayName("Deve definir e obter ID (herdado de Pessoa)")
    void shouldSetAndGetIdFromPessoa() {
        FuncionarioSaude funcionario = new FuncionarioSaude();
        funcionario.setId(1L);
        assertEquals(1L, funcionario.getId());
    }

    @Test
    @DisplayName("Deve definir e obter nome (herdado de Pessoa)")
    void shouldSetAndGetNameFromPessoa() {
        FuncionarioSaude funcionario = new FuncionarioSaude();
        funcionario.setNome("Ana Souza");
        assertEquals("Ana Souza", funcionario.getNome());
    }

    @Test
    @DisplayName("Deve definir e obter CPF (herdado de Pessoa)")
    void shouldSetAndGetCpfFromPessoa() {
        FuncionarioSaude funcionario = new FuncionarioSaude();
        funcionario.setCpf("987.654.321-00");
        assertEquals("987.654.321-00", funcionario.getCpf());
    }

    @Test
    @DisplayName("Deve definir e obter data de nascimento (herdado de Pessoa)")
    void shouldSetAndGetDataNascimentoFromPessoa() {
        FuncionarioSaude funcionario = new FuncionarioSaude();
        LocalDate dataNascimento = LocalDate.of(1980, 1, 1);
        funcionario.setDataNascimento(dataNascimento);
        assertEquals(dataNascimento, funcionario.getDataNascimento());
    }

    @Test
    @DisplayName("Deve definir e obter telefones (herdado de Pessoa)")
    void shouldSetAndGetTelefonesFromPessoa() {
        FuncionarioSaude funcionario = new FuncionarioSaude();
        List<Telefone> telefones = Collections.singletonList(telefone);
        funcionario.setTelefones(telefones);

        assertNotNull(funcionario.getTelefones());
        assertFalse(funcionario.getTelefones().isEmpty());
        assertEquals(1, funcionario.getTelefones().size());
        assertEquals(telefone, funcionario.getTelefones().get(0));
    }

    @Test
    @DisplayName("Deve definir e obter email (herdado de Pessoa)")
    void shouldSetAndGetEmailFromPessoa() {
        FuncionarioSaude funcionario = new FuncionarioSaude();
        funcionario.setEmail("ana.souza@example.com");
        assertEquals("ana.souza@example.com", funcionario.getEmail());
    }

    @Test
    @DisplayName("Deve gerar toString corretamente")
    void shouldGenerateToStringCorrectly() {
        List<String> especialidades = Arrays.asList("Geral");
        FuncionarioSaude funcionario = FuncionarioSaude.builder()
                .id(2L)
                .nome("Carlos").cpf("123.123.123-12")
                .dataNascimento(LocalDate.of(1975, 3, 10))
                .telefones(Collections.singletonList(telefone))
                .email("carlos@example.com")
                .cargo(Cargo.MEDICO)
                .especialidades(especialidades)
                .identificacao("MED789")
                .atendimentos(Collections.singletonList(atendimentoMock))
                .atendimentosComplicacao(Collections.singletonList(atendimentoMock))
                .build();

        String funcionarioToString = funcionario.toString();

        // Verificações para os campos próprios de FuncionarioSaude
        assertTrue(funcionarioToString.contains("cargo=MEDICO"));
        assertTrue(funcionarioToString.contains("especialidades=[Geral]"));
        assertTrue(funcionarioToString.contains("identificacao=MED789"));

        // Verificações para os campos de Pessoa (via super.toString() - requer @ToString(callSuper=true))
        assertTrue(funcionarioToString.contains("super=Pessoa("));
        assertTrue(funcionarioToString.contains("id=2"));
        assertTrue(funcionarioToString.contains("nome=Carlos"));
        assertTrue(funcionarioToString.contains("cpf=123.123.123-12"));
        assertTrue(funcionarioToString.contains("dataNascimento=1975-03-10"));
        assertTrue(funcionarioToString.contains("email=carlos@example.com"));
        String expectedTelefoneToString = telefone.toString();
        assertTrue(funcionarioToString.contains("telefones=[" + expectedTelefoneToString + "]"));
    }

    @Test
    @DisplayName("Dois FuncionarioSaude com o mesmo ID devem ser iguais")
    void shouldBeEqualIfIdsAreSame() {
        FuncionarioSaude f1 = FuncionarioSaude.builder().id(1L).nome("Teste").build();
        FuncionarioSaude f2 = FuncionarioSaude.builder().id(1L).nome("Outro Teste").build();

        assertEquals(f1, f2);
        assertEquals(f1.hashCode(), f2.hashCode());
    }

    @Test
    @DisplayName("Dois FuncionarioSaude com IDs diferentes não devem ser iguais")
    void shouldNotBeEqualIfIdsAreDifferent() {
        FuncionarioSaude f1 = FuncionarioSaude.builder().id(1L).nome("Teste").build();
        FuncionarioSaude f2 = FuncionarioSaude.builder().id(2L).nome("Teste").build();

        assertNotEquals(f1, f2);
        assertNotEquals(f1.hashCode(), f2.hashCode());
    }

    @Test
    @DisplayName("FuncionarioSaude deve ser igual a si mesmo")
    void shouldBeEqualToItself() {
        FuncionarioSaude funcionario = FuncionarioSaude.builder().id(1L).build();
        assertEquals(funcionario, funcionario);
    }

    @Test
    @DisplayName("FuncionarioSaude não deve ser igual a null")
    void shouldNotBeEqualToNull() {
        FuncionarioSaude funcionario = FuncionarioSaude.builder().id(1L).build();
        assertNotEquals(null, funcionario);
    }

    @Test
    @DisplayName("FuncionarioSaude não deve ser igual a outro tipo de objeto")
    void shouldNotBeEqualToDifferentTypeOfObject() {
        FuncionarioSaude funcionario = FuncionarioSaude.builder().id(1L).build();
        assertNotEquals(funcionario, new Object());
    }
}
