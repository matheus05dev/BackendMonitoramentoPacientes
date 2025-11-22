package com.springwalker.back.core.handler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@Import({GlobalExceptionHandler.class, GlobalExceptionHandlerTest.TestController.class})
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    // Controller de teste para lançar exceções específicas
    @RestController
    static class TestController {
        @GetMapping("/test/not-found")
        public void throwNoSuchElementException() {
            throw new NoSuchElementException("Recurso não encontrado.");
        }

        @GetMapping("/test/bad-request")
        public void throwIllegalArgumentException() {
            throw new IllegalArgumentException("Argumento inválido.");
        }

        @GetMapping("/test/conflict")
        public void throwIllegalStateException() {
            throw new IllegalStateException("Estado inválido.");
        }

        @GetMapping("/test/unauthorized")
        public void throwAuthenticationException() {
            throw new AuthenticationException("Não autenticado.") {};
        }

        @GetMapping("/test/internal-error")
        public void throwGenericException() throws Exception {
            throw new Exception("Erro interno genérico.");
        }
    }

    @Test
    void whenNoSuchElementException_thenReturns404() throws Exception {
        mockMvc.perform(get("/test/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.error", is("Not Found")))
                .andExpect(jsonPath("$.message", is("Recurso não encontrado.")))
                .andExpect(jsonPath("$.path", is("/test/not-found")));
    }

    @Test
    void whenIllegalArgumentException_thenReturns400() throws Exception {
        mockMvc.perform(get("/test/bad-request"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Bad Request")))
                .andExpect(jsonPath("$.message", is("Argumento inválido.")))
                .andExpect(jsonPath("$.path", is("/test/bad-request")));
    }

    @Test
    void whenIllegalStateException_thenReturns409() throws Exception {
        mockMvc.perform(get("/test/conflict"))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(409)))
                .andExpect(jsonPath("$.error", is("Conflict")))
                .andExpect(jsonPath("$.message", is("Estado inválido.")))
                .andExpect(jsonPath("$.path", is("/test/conflict")));
    }

    @Test
    void whenAuthenticationException_thenReturns401() throws Exception {
        mockMvc.perform(get("/test/unauthorized"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(401)))
                .andExpect(jsonPath("$.error", is("Unauthorized")))
                .andExpect(jsonPath("$.message", is("Não autenticado.")))
                .andExpect(jsonPath("$.path", is("/test/unauthorized")));
    }

    @Test
    void whenGenericException_thenReturns500() throws Exception {
        mockMvc.perform(get("/test/internal-error"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(500)))
                .andExpect(jsonPath("$.error", is("Internal Server Error")))
                .andExpect(jsonPath("$.message", is("Erro interno genérico.")))
                .andExpect(jsonPath("$.path", is("/test/internal-error")));
    }
}
