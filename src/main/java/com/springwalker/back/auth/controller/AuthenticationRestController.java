package com.springwalker.back.auth.controller;


import com.springwalker.back.auth.services.TokenService;
import com.springwalker.back.user.model.User;
import com.springwalker.back.auth.dto.DadosAutenticacao;
import com.springwalker.back.auth.dto.DadosTokenJWT;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth/login")
@Tag(name = "Autenticação", description = "API para autenticação de usuários e geração de token JWT")
public class AuthenticationRestController {


    private final AuthenticationManager manager;
    private final TokenService tokenService;

    @PostMapping
    @Operation(summary = "Autentica um usuário e retorna um token JWT",
            description = "Recebe as credenciais do usuário (login e senha) e, se válidas, retorna um token JWT para acesso às APIs protegidas.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Autenticação bem-sucedida e token JWT gerado"),
                    @ApiResponse(responseCode = "400", description = "Credenciais inválidas ou requisição malformada")
            })
    public ResponseEntity<DadosTokenJWT> efetuarLogin(@RequestBody @Valid DadosAutenticacao dados) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
        var authentication = manager.authenticate(authenticationToken);

        var tokenJWT = tokenService.gerarToken((User) authentication.getPrincipal());

        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
    }
}
