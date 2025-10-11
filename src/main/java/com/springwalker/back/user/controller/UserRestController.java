package com.springwalker.back.user.controller;

import com.springwalker.back.user.dto.UserRequestDTO;
import com.springwalker.back.user.dto.UserResponseDTO;
import com.springwalker.back.user.service.AlteraUserService;
import com.springwalker.back.user.service.BuscarUserService;
import com.springwalker.back.user.service.CriarUserService;
import com.springwalker.back.user.service.DeletarUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "API para gerenciamento de usuários")
public class UserRestController {

    private final CriarUserService criarUserService;
    private final AlteraUserService alteraUserService;
    private final BuscarUserService buscarUserService;
    private final DeletarUserService deletarUserService;

    //Criar usuário
    @PostMapping
    @Operation(summary = "Cria um novo usuário",
            description = "Cria um novo usuário com os dados fornecidos.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário criado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Dados de requisição inválidos")
            })
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO user = criarUserService.execute(userRequestDTO);
        return ResponseEntity.ok(user);
    }
    //Alterar usuário
    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um usuário existente",
            description = "Atualiza os dados de um usuário existente pelo ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
                    @ApiResponse(responseCode = "400", description = "Dados de requisição inválidos")
            })
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO user = alteraUserService.execute(id, userRequestDTO);
        return ResponseEntity.ok(user);
    }
    //Buscar usuário
    @GetMapping
    @Operation(summary = "Lista todos os usuários",
            description = "Retorna uma lista de todos os usuários cadastrados.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso")
            })
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = buscarUserService.buscarTodos();
        return ResponseEntity.ok(users);
    }
    //Buscar usuário por ID
    @GetMapping("/id/{id}")
    @Operation(summary = "Busca um usuário por ID",
            description = "Retorna um usuário específico pelo seu ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
            })
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        UserResponseDTO user = buscarUserService.buscarPorId(id).orElse(null);
        return ResponseEntity.ok(user);
    }

    //Buscar usuário por username
    @GetMapping("/username/{username}")
    @Operation(summary = "Busca um usuário por username",
            description = "Retorna um usuário específico pelo seu nome de usuário.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
            })
    public ResponseEntity<UserResponseDTO> getUserByUsername(@PathVariable String username) {
        UserResponseDTO user = buscarUserService.buscarPorUsername(username).orElse(null);
        return ResponseEntity.ok(user);
    }

    //Deleta usuário
    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta um usuário",
            description = "Deleta um usuário existente pelo ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
            })
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        deletarUserService.execute(id);
        return ResponseEntity.noContent().build();
    }
}
