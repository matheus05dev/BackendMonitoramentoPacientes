package com.springwalker.back.user.controller;

import com.springwalker.back.core.log.service.LogService; // Importar LogService
import com.springwalker.back.user.dto.UserRequestDTO;
import com.springwalker.back.user.dto.UserResponseDTO;
import com.springwalker.back.user.service.AlteraUserService;
import com.springwalker.back.user.service.BuscarUserService;
import com.springwalker.back.user.service.CriarUserService;
import com.springwalker.back.user.service.DeletarUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "API para gerenciamento de usuários")
public class UserRestController {

    private final CriarUserService criarUserService;
    private final AlteraUserService alteraUserService;
    private final BuscarUserService buscarUserService;
    private final DeletarUserService deletarUserService;
    private final LogService logService; // Injetar LogService

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Cria um novo usuário")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO user = criarUserService.execute(userRequestDTO);
        logService.logEvent("CRIACAO_USUARIO", "Criação de novo usuário: " + user.id());
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Atualiza um usuário existente")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @RequestBody UserRequestDTO userRequestDTO) {
        logService.logEvent("ALTERACAO_USUARIO", "Alteração de usuário: " + id);
        UserResponseDTO user = alteraUserService.execute(id, userRequestDTO);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Lista todos os usuários")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        logService.logEvent("LISTAGEM_USUARIOS", "Listagem de todos os usuários");
        List<UserResponseDTO> users = buscarUserService.buscarTodos();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Busca um usuário por ID")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        logService.logEvent("BUSCA_USUARIO", "Busca de usuário por ID: " + id);
        return ResponseEntity.ok(buscarUserService.buscarPorId(id)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado com o ID: " + id)));
    }

    @GetMapping("/username/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Busca um usuário por username")
    public ResponseEntity<UserResponseDTO> getUserByUsername(@PathVariable String username) {
        logService.logEvent("BUSCA_USUARIO_USERNAME", "Busca de usuário por username: " + username);
        return ResponseEntity.ok(buscarUserService.buscarPorUsername(username)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado com o username: " + username)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deleta um usuário")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        logService.logEvent("EXCLUSAO_USUARIO", "Exclusão de usuário: " + id);
        deletarUserService.execute(id);
        return ResponseEntity.noContent().build();
    }
}
