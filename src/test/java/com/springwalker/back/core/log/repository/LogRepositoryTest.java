package com.springwalker.back.core.log.repository;

import com.springwalker.back.core.log.model.Log;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class LogRepositoryTest {

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Salvar e encontrar log")
    public void testSaveAndFindLog() {
        Log log = new Log();
        log.setDescription("Test log entry");
        log.setTimestamp(java.time.LocalDateTime.now());
        log.setEventType("INFO");

        Log savedLog = logRepository.save(log);

        assertThat(savedLog).isNotNull();
        assertThat(savedLog.getId()).isNotNull();
        assertThat(savedLog.getDescription()).isEqualTo("Test log entry");
        assertThat(savedLog.getEventType()).isEqualTo("INFO");

        Log foundLog = entityManager.find(Log.class, savedLog.getId());
        assertThat(foundLog).isNotNull();
        assertThat(foundLog.getDescription()).isEqualTo("Test log entry");
        assertThat(foundLog.getEventType()).isEqualTo("INFO"); // Assert eventType
    }
}
