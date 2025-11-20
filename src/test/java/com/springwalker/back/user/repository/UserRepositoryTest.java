package com.springwalker.back.user.repository;

import com.springwalker.back.user.model.User;
import com.springwalker.back.user.role.Role;
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
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Deve persistir um usuário com sucesso")
    void shouldPersistUserSuccessfully() {
        User user = new User("testuser", "password", Role.ADMIN);
        User savedUser = userRepository.save(user);

        assertNotNull(savedUser.getId());
        assertEquals("testuser", savedUser.getUsername());
        assertEquals("password", savedUser.getPassword());
        assertEquals(Role.ADMIN, savedUser.getRole());

        User foundUser = entityManager.find(User.class, savedUser.getId());
        assertNotNull(foundUser);
        assertEquals(savedUser.getId(), foundUser.getId());
    }

    @Test
    @DisplayName("Deve encontrar um usuário por nome de usuário")
    void shouldFindUserByUsername() {
        User user = new User("findbyusername", "password", Role.MEDICO);
        entityManager.persistAndFlush(user);

        Optional<User> foundUser = userRepository.findByUsername("findbyusername");

        assertTrue(foundUser.isPresent());
        assertEquals(user.getUsername(), foundUser.get().getUsername());
    }

    @Test
    @DisplayName("Deve retornar vazio quando o usuário não for encontrado por nome de usuário")
    void shouldReturnEmptyWhenUserNotFoundByUsername() {
        Optional<User> foundUser = userRepository.findByUsername("nonexistent");
        assertFalse(foundUser.isPresent());
    }

    @Test
    @DisplayName("Deve encontrar um usuário por ID")
    void shouldFindUserById() {
        User user = new User("findbyid", "password", Role.ENFERMEIRO);
        User persistedUser = entityManager.persistAndFlush(user);

        Optional<User> foundUser = userRepository.findById(persistedUser.getId());

        assertTrue(foundUser.isPresent());
        assertEquals(persistedUser.getId(), foundUser.get().getId());
    }

    @Test
    @DisplayName("Deve retornar vazio quando o usuário não for encontrado por ID")
    void shouldReturnEmptyWhenUserNotFoundById() {
        Optional<User> foundUser = userRepository.findById(999L);
        assertFalse(foundUser.isPresent());
    }

    @Test
    @DisplayName("Deve deletar um usuário por ID")
    void shouldDeleteUserById() {
        User user = new User("todelete", "password", Role.AUXILIAR_ENFERMAGEM);
        User persistedUser = entityManager.persistAndFlush(user);

        userRepository.deleteById(persistedUser.getId());

        User deletedUser = entityManager.find(User.class, persistedUser.getId());
        assertNull(deletedUser);
    }

    @Test
    @DisplayName("Deve verificar se um usuário existe por nome de usuário")
    void shouldCheckIfUserExistsByUsername() {
        User user = new User("existuser", "password", Role.TECNICO_ENFERMAGEM);
        entityManager.persistAndFlush(user);

        boolean exists = userRepository.existsByUsername("existuser");
        assertTrue(exists);

        boolean notExists = userRepository.existsByUsername("nonexistent");
        assertFalse(notExists);
    }
}
