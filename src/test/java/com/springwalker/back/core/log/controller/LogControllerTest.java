package com.springwalker.back.core.log.controller;

import com.springwalker.back.core.auth.services.TokenService;
import com.springwalker.back.core.config.security.SecurityFilter; // Import SecurityFilter
import com.springwalker.back.core.log.model.Log;
import com.springwalker.back.core.log.repository.LogRepository;
import com.springwalker.back.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(LogController.class)
@AutoConfigureMockMvc(addFilters = false)
public class LogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LogRepository logRepository;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean // Mock the SecurityFilter itself
    private SecurityFilter securityFilter;

    @Test
    @DisplayName("Listar todos os logs") // Added DisplayName
    public void testListAllLogs() throws Exception {
        Log log = new Log();
        log.setId(1L);
        log.setDescription("Test Log");
        List<Log> logs = Collections.singletonList(log);

        when(logRepository.findAll()).thenReturn(logs);

        mockMvc.perform(get("/logs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].description").value("Test Log"));
    }
}
