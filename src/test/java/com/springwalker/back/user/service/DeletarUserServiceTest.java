package com.springwalker.back.user.service;

import com.springwalker.back.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DeletarUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DeletarUserService deletarUserService;

    private AutoCloseable closeable;

    @BeforeEach
    void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    @DisplayName("Deletar usuário com sucesso")
    void execute() {
        when(userRepository.existsById(1L)).thenReturn(true);

        deletarUserService.execute(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Lançar exceção quando o usuário não for encontrado")
    void executeUserNotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> deletarUserService.execute(1L));
        verify(userRepository, never()).deleteById(1L);
    }
}
