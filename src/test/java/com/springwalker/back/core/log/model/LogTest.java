package com.springwalker.back.core.log.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class LogTest {

    @Test
    @DisplayName("Testar Getters e Setters")
    public void testGettersAndSetters() {
        Log log = new Log();

        Long id = 1L;
        String description = "Test Description";
        LocalDateTime timestamp = LocalDateTime.now();
        String eventType = "INFO";
        Long userId = 101L; // Adicionado userId

        log.setId(id);
        log.setDescription(description);
        log.setTimestamp(timestamp);
        log.setEventType(eventType);
        log.setUserId(userId); // Adicionado setUserId

        assertThat(log.getId()).isEqualTo(id);
        assertThat(log.getDescription()).isEqualTo(description);
        assertThat(log.getTimestamp()).isEqualTo(timestamp);
        assertThat(log.getEventType()).isEqualTo(eventType);
        assertThat(log.getUserId()).isEqualTo(userId); // Adicionado asserção para userId
    }

    @Test
    @DisplayName("Testar Construtor Sem Argumentos")
    public void testNoArgsConstructor() {
        Log log = new Log();
        assertThat(log).isNotNull();
    }

    @Test
    @DisplayName("Testar Construtor Com Todos os Argumentos")
    public void testAllArgsConstructor() {
        Long id = 2L;
        String description = "Another Description";
        LocalDateTime timestamp = LocalDateTime.now().minusHours(1);
        String eventType = "DEBUG";
        Long userId = 102L; 

        // O construtor para Log agora é (Long id, LocalDateTime timestamp, String eventType, Long userId, String description)
        Log log = new Log(id, timestamp, eventType, userId, description);

        assertThat(log.getId()).isEqualTo(id);
        assertThat(log.getDescription()).isEqualTo(description);
        assertThat(log.getTimestamp()).isEqualTo(timestamp);
        assertThat(log.getEventType()).isEqualTo(eventType);
        assertThat(log.getUserId()).isEqualTo(userId); // Adicionado asserção para userId
    }
}
