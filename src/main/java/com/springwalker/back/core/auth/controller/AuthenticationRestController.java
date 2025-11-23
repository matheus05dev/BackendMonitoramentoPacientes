package com.springwalker.back.core.auth.controller;

import com.springwalker.back.core.auth.dto.DadosAutenticacao;
import com.springwalker.back.core.auth.dto.DadosRefreshToken;
import com.springwalker.back.core.auth.dto.DadosTokenJWT;
import com.springwalker.back.core.auth.services.TokenService;
import com.springwalker.back.core.log.service.LogService;
import com.springwalker.back.user.model.User;
import com.springwalker.back.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "API para autenticação de usuários e geração de token JWT")
public class AuthenticationRestController {

    private final AuthenticationManager manager;
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final LogService logService;

    @PostMapping("/login")
    @Operation(summary = "Autentica um usuário e retorna tokens de acesso e atualização",
            description = "Recebe as credenciais do usuário (login e senha) e, se válidas, retorna um Access Token e um Refresh Token.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Autenticação bem-sucedida e tokens gerados"),
                    @ApiResponse(responseCode = "400", description = "Credenciais inválidas ou requisição malformada")
            })
    public ResponseEntity<DadosTokenJWT> efetuarLogin(@RequestBody @Valid DadosAutenticacao dados) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
        var authentication = manager.authenticate(authenticationToken);
        var user = (User) authentication.getPrincipal();

        logService.logEvent("SUCESSO_AUTENTICACAO", "Usuário autenticado com sucesso: " + user.getUsername());

        var accessToken = tokenService.gerarToken(user);
        var refreshToken = tokenService.gerarRefreshToken(user);

        return ResponseEntity.ok(new DadosTokenJWT(accessToken, refreshToken));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Gera um novo access token a partir de um refresh token",
            description = "Recebe um refresh token válido e retorna um novo access token.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Novo access token gerado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Refresh token inválido ou expirado")
            })
    public ResponseEntity<DadosTokenJWT> refreshToken(@RequestBody @Valid DadosRefreshToken dados) {
        var subject = tokenService.getSubject(dados.refreshToken());
        var user = userRepository.findByUsername(subject)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        var newAccessToken = tokenService.gerarToken(user);

        logService.logEvent("ATUALIZACAO_TOKEN", "Token do usuário atualizado: " + user.getUsername());

        // Retornamos o novo access token. O refresh token não é retornado aqui.
        return ResponseEntity.ok(new DadosTokenJWT(newAccessToken, null));
    }

    @PostMapping("/logout")
    @Operation(summary = "Efetua o logout do usuário",
            description = "Endpoint para o cliente sinalizar o logout. O cliente deve destruir o token JWT localmente.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Logout sinalizado com sucesso")
            })
    public ResponseEntity<Void> logout() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "anônimo";
        if (principal instanceof User) {
            username = ((User) principal).getUsername();
        } else if (principal instanceof String) {
            username = (String) principal;
        }

        logService.logEvent("LOGOUT", "Usuário desconectado: " + username);
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().build();
    }
}
