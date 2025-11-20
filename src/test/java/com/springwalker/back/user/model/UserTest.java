package com.springwalker.back.user.model;

import com.springwalker.back.user.role.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User userAdmin;
    private User userMedico;

    @BeforeEach
    void setUp() {
        userAdmin = new User(1L, "adminUser", "adminPass", Role.ADMIN);
        userMedico = new User(2L, "medicoUser", "medicoPass", Role.MEDICO);
    }

    @Test
    @DisplayName("Deve criar um User com o construtor vazio")
    void shouldCreateUserWithNoArgsConstructor() {
        User user = new User();
        assertNotNull(user);
        assertNull(user.getId());
        assertNull(user.getUsername());
        assertNull(user.getPassword());
        assertNull(user.getRole());
    }


    @Test
    @DisplayName("Deve criar um User com o construtor completo (todos os campos)")
    void shouldCreateUserWithAllArgsConstructor() {
        assertEquals(1L, userAdmin.getId());
        assertEquals("adminUser", userAdmin.getUsername());
        assertEquals("adminPass", userAdmin.getPassword());
        assertEquals(Role.ADMIN, userAdmin.getRole());
    }

    @Test
    @DisplayName("Deve criar um User com o construtor de três argumentos (username, password, role)")
    void shouldCreateUserWithThreeArgsConstructor() {
        User newUser = new User("testuser", "testpass", Role.ENFERMEIRO);
        assertNull(newUser.getId()); // ID should be null before persistence
        assertEquals("testuser", newUser.getUsername());
        assertEquals("testpass", newUser.getPassword());
        assertEquals(Role.ENFERMEIRO, newUser.getRole());

    }

    @Test
    @DisplayName("Deve definir e obter ID")
    void shouldSetAndGetId() {
        User user = new User();
        user.setId(5L);
        assertEquals(5L, user.getId());
    }

    @Test
    @DisplayName("Deve definir e obter username")
    void shouldSetAndGetUsername() {
        User user = new User();
        user.setUsername("newUsername");
        assertEquals("newUsername", user.getUsername());
    }

    @Test
    @DisplayName("Deve definir e obter password")
    void shouldSetAndGetPassword() {
        User user = new User();
        user.setPassword("newPassword");
        assertEquals("newPassword", user.getPassword());
    }

    @Test
    @DisplayName("Deve definir e obter role")
    void shouldSetAndGetRole() {
        User user = new User();
        user.setRole(Role.TECNICO_ENFERMAGEM);
        assertEquals(Role.TECNICO_ENFERMAGEM, user.getRole());
    }

    @Test
    @DisplayName("Deve retornar as GrantedAuthorities corretas para ADMIN")
    void shouldReturnCorrectAuthoritiesForAdmin() {
        Collection<? extends GrantedAuthority> authorities = userAdmin.getAuthorities();
        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

    @Test
    @DisplayName("Deve retornar as GrantedAuthorities corretas para MEDICO")
    void shouldReturnCorrectAuthoritiesForMedico() {
        Collection<? extends GrantedAuthority> authorities = userMedico.getAuthorities();
        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_MEDICO")));
    }

    @Test
    @DisplayName("isAccountNonExpired deve retornar true")
    void isAccountNonExpiredShouldReturnTrue() {
        assertTrue(userAdmin.isAccountNonExpired());
    }

    @Test
    @DisplayName("isAccountNonLocked deve retornar true")
    void isAccountNonLockedShouldReturnTrue() {
        assertTrue(userAdmin.isAccountNonLocked());
    }

    @Test
    @DisplayName("isCredentialsNonExpired deve retornar true")
    void isCredentialsNonExpiredShouldReturnTrue() {
        assertTrue(userAdmin.isCredentialsNonExpired());
    }

    @Test
    @DisplayName("isEnabled deve retornar true")
    void isEnabledShouldReturnTrue() {
        assertTrue(userAdmin.isEnabled());
    }

    @Test
    @DisplayName("Deve gerar toString corretamente")
    void shouldGenerateToStringCorrectly() {
        String userToString = userAdmin.toString();
        assertTrue(userToString.contains("id=1"));
        assertTrue(userToString.contains("username=adminUser"));
        assertFalse(userToString.contains("password")); // Corrected: password should not be in toString()
        assertTrue(userToString.contains("role=ADMIN"));
    }

    @Test
    @DisplayName("Dois Users com o mesmo ID devem ser iguais")
    void shouldBeEqualIfIdsAreSame() {
        User user1 = new User(1L, "userA", "passA", Role.ADMIN);
        User user2 = new User(1L, "userB", "passB", Role.MEDICO); // Different username/password/role but same ID

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    @DisplayName("Dois Users com IDs diferentes não devem ser iguais")
    void shouldNotBeEqualIfIdsAreDifferent() {
        User user1 = new User(1L, "userA", "passA", Role.ADMIN);
        User user2 = new User(2L, "userA", "passA", Role.ADMIN);

        assertNotEquals(user1, user2);
        assertNotEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    @DisplayName("User deve ser igual a si mesmo")
    void shouldBeEqualToItself() {
        assertEquals(userAdmin, userAdmin);
    }

    @Test
    @DisplayName("User não deve ser igual a null")
    void shouldNotBeEqualToNull() {
        assertNotEquals(null, userAdmin);
    }

    @Test
    @DisplayName("User não deve ser igual a outro tipo de objeto")
    void shouldNotBeEqualToDifferentTypeOfObject() {
        assertNotEquals(userAdmin, new Object());
    }
}
