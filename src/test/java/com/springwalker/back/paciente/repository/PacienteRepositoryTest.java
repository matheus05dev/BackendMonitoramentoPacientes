package com.springwalker.back.paciente.repository;

import com.springwalker.back.paciente.model.Paciente;
import com.springwalker.back.pessoa.enums.Sexo;
import com.springwalker.back.quarto.enums.LocalizacaoQuarto;
import com.springwalker.back.quarto.enums.TipoQuarto;
import com.springwalker.back.quarto.model.Quarto;
import com.springwalker.back.quarto.repository.QuartoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test") // Garante que o perfil de teste seja usado, se houver configurações específicas
class PacienteRepositoryTest {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private QuartoRepository quartoRepository; // Para persistir o Quarto

    @Autowired
    private TestEntityManager entityManager; // Para persistir entidades diretamente no contexto de teste

    private Quarto quarto;
    private Paciente paciente1;
    private Paciente paciente2;

    @BeforeEach
    void setUp() {
        // Limpa o banco de dados antes de cada teste
        pacienteRepository.deleteAll();
        quartoRepository.deleteAll();
        entityManager.clear();

        // Cria e persiste um Quarto para ser associado aos pacientes
        quarto = new Quarto();
        quarto.setNumero(101);
        quarto.setCapacidade(2);
        quarto.setLocalizacao(LocalizacaoQuarto.SETOR_NORTE); // Set a valid value
        quarto.setTipo(TipoQuarto.ENFERMARIA); // Set a valid value
        entityManager.persist(quarto);
        entityManager.flush(); // Garante que o quarto seja persistido antes de ser usado

        // Cria e persiste pacientes de teste
        paciente1 = new Paciente();
        paciente1.setNome("João Silva");
        paciente1.setEmail("joao.silva@example.com");
        paciente1.setSexo(Sexo.Masculino);
        paciente1.setDataNascimento(LocalDate.of(1990, 1, 1));
        paciente1.setCpf("690.438.980-07");
        paciente1.setAlergias(List.of("Alergia a amendoim"));
        paciente1.setQuarto(quarto); // Associa ao quarto
        entityManager.persist(paciente1);

        paciente2 = new Paciente();
        paciente2.setNome("Maria Oliveira");
        paciente2.setEmail("maria.oliveira@example.com");
        paciente2.setSexo(Sexo.Feminino);
        paciente2.setDataNascimento(LocalDate.of(1985, 5, 10));
        paciente2.setCpf("850.555.330-64");
        paciente2.setAlergias(List.of("Nenhuma"));
        entityManager.persist(paciente2);

        entityManager.flush(); // Garante que os pacientes sejam persistidos
    }

    @Test
    @DisplayName("Deve salvar um paciente com sucesso")
    void savePaciente_success() {
        Paciente novoPaciente = new Paciente();
        novoPaciente.setNome("Carlos Souza");
        novoPaciente.setEmail("carlos.souza@example.com");
        novoPaciente.setSexo(Sexo.Masculino);
        novoPaciente.setDataNascimento(LocalDate.of(1992, 3, 15));
        novoPaciente.setCpf("616.149.470-15");

        Paciente pacienteSalvo = pacienteRepository.save(novoPaciente);

        assertNotNull(pacienteSalvo.getId());
        assertEquals("Carlos Souza", pacienteSalvo.getNome());
        Optional<Paciente> found = pacienteRepository.findById(pacienteSalvo.getId());
        assertTrue(found.isPresent());
        assertEquals("Carlos Souza", found.get().getNome());
    }

    @Test
    @DisplayName("Deve encontrar paciente por CPF")
    void findByCpf_shouldReturnPaciente() {
        Optional<Paciente> found = pacienteRepository.findByCpf("690.438.980-07");
        assertTrue(found.isPresent());
        assertEquals("João Silva", found.get().getNome());
    }

    @Test
    @DisplayName("Não deve encontrar paciente por CPF inexistente")
    void findByCpf_shouldReturnEmptyOptional() {
        Optional<Paciente> found = pacienteRepository.findByCpf("000.000.000-00");
        assertFalse(found.isPresent());
    }

    @Test
    @DisplayName("Deve verificar a existência de paciente por CPF")
    void existsByCpf_shouldReturnTrue() {
        assertTrue(pacienteRepository.existsByCpf("690.438.980-07"));
    }

    @Test
    @DisplayName("Deve verificar a não existência de paciente por CPF")
    void existsByCpf_shouldReturnFalse() {
        assertFalse(pacienteRepository.existsByCpf("000.000.000-00"));
    }

    @Test
    @DisplayName("Deve encontrar pacientes por nome contendo a string")
    void findPacientesByNomeContaining_shouldReturnMatchingPacientes() {
        List<Paciente> pacientes = pacienteRepository.findPacientesByNomeContaining("João");
        assertFalse(pacientes.isEmpty());
        assertEquals(1, pacientes.size());
        assertEquals("João Silva", pacientes.get(0).getNome());

        pacientes = pacienteRepository.findPacientesByNomeContaining("Silva");
        assertFalse(pacientes.isEmpty());
        assertEquals(1, pacientes.size());
        assertEquals("João Silva", pacientes.get(0).getNome());

        pacientes = pacienteRepository.findPacientesByNomeContaining("a"); // "João Silva" e "Maria Oliveira"
        assertEquals(2, pacientes.size());
    }

    @Test
    @DisplayName("Não deve encontrar pacientes por nome contendo string inexistente")
    void findPacientesByNomeContaining_shouldReturnEmptyList() {
        List<Paciente> pacientes = pacienteRepository.findPacientesByNomeContaining("Inexistente");
        assertTrue(pacientes.isEmpty());
    }

    @Test
    @DisplayName("Deve encontrar paciente por ID com Quarto carregado")
    void findByIdWithQuarto_shouldReturnPacienteWithQuarto() {
        Optional<Paciente> found = pacienteRepository.findByIdWithQuarto(paciente1.getId());
        assertTrue(found.isPresent());
        assertEquals("João Silva", found.get().getNome());
        assertNotNull(found.get().getQuarto());
        assertEquals(quarto.getId(), found.get().getQuarto().getId());
    }

    @Test
    @DisplayName("Deve retornar Optional vazio para ID inexistente com Quarto")
    void findByIdWithQuarto_shouldReturnEmptyOptionalForNonExistingId() {
        Optional<Paciente> found = pacienteRepository.findByIdWithQuarto(999L);
        assertFalse(found.isPresent());
    }

    @Test
    @DisplayName("Deve encontrar todos os pacientes")
    void findAll_shouldReturnAllPacientes() {
        List<Paciente> pacientes = pacienteRepository.findAll();
        assertEquals(2, pacientes.size());
    }

    @Test
    @DisplayName("Deve deletar um paciente por ID")
    void deleteById_shouldRemovePaciente() {
        Long idToDelete = paciente1.getId();
        pacienteRepository.deleteById(idToDelete);
        Optional<Paciente> found = pacienteRepository.findById(idToDelete);
        assertFalse(found.isPresent());
    }
}
