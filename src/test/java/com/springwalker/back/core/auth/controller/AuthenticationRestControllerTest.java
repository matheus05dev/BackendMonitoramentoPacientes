package com.springwalker.back.core.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springwalker.back.core.auth.dto.DadosAutenticacao;
import com.springwalker.back.core.auth.services.TokenService;
import com.springwalker.back.core.config.security.SecurityConfig;
import com.springwalker.back.user.model.User;
import com.springwalker.back.user.repository.UserRepository;
import com.springwalker.back.user.role.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationRestController.class)
@Import(SecurityConfig.class)
class AuthenticationRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Efetuar login com sucesso")
    void efetuarLogin() throws Exception {
        DadosAutenticacao dadosAutenticacao = new DadosAutenticacao("test.user", "password");
        User user = new User("test.user", "password", Role.ADMIN);
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null);
        String token = "test.token";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(tokenService.gerarToken(user)).thenReturn(token);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dadosAutenticacao)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(token));
    }
}
