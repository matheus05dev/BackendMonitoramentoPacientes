package com.springwalker.back.core.log.service;

import com.springwalker.back.core.log.model.Log;
import com.springwalker.back.core.log.repository.LogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class LogServiceTest {

    @Mock
    private LogRepository logRepository;

    @InjectMocks
    private LogService logService;

    @Test
    public void testLogEvent() {
        String eventType = "Test Event";
        String description = "This is a test description";

        logService.logEvent(eventType, description);

        ArgumentCaptor<Log> logArgumentCaptor = ArgumentCaptor.forClass(Log.class);
        verify(logRepository).save(logArgumentCaptor.capture());

        Log capturedLog = logArgumentCaptor.getValue();
        assertNotNull(capturedLog.getTimestamp());
        assertEquals(eventType, capturedLog.getEventType());
        assertEquals(description, capturedLog.getDescription());
    }
}
