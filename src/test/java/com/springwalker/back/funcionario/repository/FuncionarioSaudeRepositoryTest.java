package com.springwalker.back.funcionario.repository;

import com.springwalker.back.funcionario.model.FuncionarioSaude;
import com.springwalker.back.pessoa.enums.Sexo;
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
@ActiveProfiles("test")
class FuncionarioSaudeRepositoryTest {

    @Autowired
    private FuncionarioSaudeRepository funcionarioSaudeRepository;

    @Autowired
    private TestEntityManager entityManager;

    private FuncionarioSaude funcionario1;
    private FuncionarioSaude funcionario2;

    @BeforeEach
    void setUp() {
        funcionarioSaudeRepository.deleteAll();
        entityManager.clear();

        funcionario1 = new FuncionarioSaude();
        funcionario1.setNome("João Silva");
        funcionario1.setEmail("joao.silva@example.com");
        funcionario1.setSexo(Sexo.Masculino);
        funcionario1.setDataNascimento(LocalDate.of(1990, 1, 1));
        funcionario1.setCpf("690.438.980-07");
        funcionario1.setEspecialidades(List.of("Cardiologista")); // Corrected
        entityManager.persist(funcionario1);

        funcionario2 = new FuncionarioSaude();
        funcionario2.setNome("Maria Oliveira");
        funcionario2.setEmail("maria.oliveira@example.com");
        funcionario2.setSexo(Sexo.Feminino);
        funcionario2.setDataNascimento(LocalDate.of(1985, 5, 10));
        funcionario2.setCpf("850.555.330-64");
        funcionario2.setEspecialidades(List.of("Enfermeira")); // Corrected
        entityManager.persist(funcionario2);

        entityManager.flush();
    }

    @Test
    @DisplayName("Deve salvar um funcionario com sucesso")
    void saveFuncionario_success() {
        FuncionarioSaude novoFuncionario = new FuncionarioSaude();
        novoFuncionario.setNome("Carlos Souza");
        novoFuncionario.setEmail("carlos.souza@example.com");
        novoFuncionario.setSexo(Sexo.Masculino);
        novoFuncionario.setDataNascimento(LocalDate.of(1992, 3, 15));
        novoFuncionario.setCpf("616.149.470-15");
        novoFuncionario.setEspecialidades(List.of("Pediatra")); // Corrected

        FuncionarioSaude funcionarioSalvo = funcionarioSaudeRepository.save(novoFuncionario);

        assertNotNull(funcionarioSalvo.getId());
        assertEquals("Carlos Souza", funcionarioSalvo.getNome());
        Optional<FuncionarioSaude> found = funcionarioSaudeRepository.findById(funcionarioSalvo.getId());
        assertTrue(found.isPresent());
        assertEquals("Carlos Souza", found.get().getNome());
    }

    @Test
    @DisplayName("Deve encontrar funcionario por CPF")
    void findFuncionarioSaudeByCpf_shouldReturnFuncionario() {
        Optional<FuncionarioSaude> found = funcionarioSaudeRepository.findFuncionarioSaudeByCpf("690.438.980-07");
        assertTrue(found.isPresent());
        assertEquals("João Silva", found.get().getNome());
    }

    @Test
    @DisplayName("Não deve encontrar funcionario por CPF inexistente")
    void findFuncionarioSaudeByCpf_shouldReturnEmptyOptional() {
        Optional<FuncionarioSaude> found = funcionarioSaudeRepository.findFuncionarioSaudeByCpf("000.000.000-00");
        assertFalse(found.isPresent());
    }

    @Test
    @DisplayName("Deve verificar a existência de funcionario por CPF")
    void existsByCpf_shouldReturnTrue() {
        assertTrue(funcionarioSaudeRepository.existsByCpf("690.438.980-07"));
    }

    @Test
    @DisplayName("Deve verificar a não existência de funcionario por CPF")
    void existsByCpf_shouldReturnFalse() {
        assertFalse(funcionarioSaudeRepository.existsByCpf("000.000.000-00"));
    }

    @Test
    @DisplayName("Deve encontrar funcionarios por nome contendo a string")
    void findFuncionarioSaudesByNomeContaining_shouldReturnMatchingFuncionarios() {
        List<FuncionarioSaude> funcionarios = funcionarioSaudeRepository.findFuncionarioSaudesByNomeContaining("João");
        assertFalse(funcionarios.isEmpty());
        assertEquals(1, funcionarios.size());
        assertEquals("João Silva", funcionarios.get(0).getNome());

        funcionarios = funcionarioSaudeRepository.findFuncionarioSaudesByNomeContaining("Silva");
        assertFalse(funcionarios.isEmpty());
        assertEquals(1, funcionarios.size());
        assertEquals("João Silva", funcionarios.get(0).getNome());

        funcionarios = funcionarioSaudeRepository.findFuncionarioSaudesByNomeContaining("a");
        assertEquals(2, funcionarios.size());
    }

    @Test
    @DisplayName("Não deve encontrar funcionarios por nome contendo string inexistente")
    void findFuncionarioSaudesByNomeContaining_shouldReturnEmptyList() {
        List<FuncionarioSaude> funcionarios = funcionarioSaudeRepository.findFuncionarioSaudesByNomeContaining("Inexistente");
        assertTrue(funcionarios.isEmpty());
    }

    @Test
    @DisplayName("Deve encontrar todos os funcionarios")
    void findAll_shouldReturnAllFuncionarios() {
        List<FuncionarioSaude> funcionarios = funcionarioSaudeRepository.findAll();
        assertEquals(2, funcionarios.size());
    }

    @Test
    @DisplayName("Deve deletar um funcionario por ID")
    void deleteById_shouldRemoveFuncionario() {
        Long idToDelete = funcionario1.getId();
        funcionarioSaudeRepository.deleteById(idToDelete);
        Optional<FuncionarioSaude> found = funcionarioSaudeRepository.findById(idToDelete);
        assertFalse(found.isPresent());
    }
}
