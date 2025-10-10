package com.springwalker.back.user.controller;

import com.springwalker.back.user.dto.UserRequestDTO;
import com.springwalker.back.user.dto.UserResponseDTO;
import com.springwalker.back.user.service.AlteraUserService;
import com.springwalker.back.user.service.BuscarUserService;
import com.springwalker.back.user.service.CriarUserService;
import com.springwalker.back.user.service.DeletarUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRestController {

    private final CriarUserService criarUserService;
    private final AlteraUserService alteraUserService;
    private final BuscarUserService buscarUserService;
    private final DeletarUserService deletarUserService;

    //Criar usuário
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO user = criarUserService.execute(userRequestDTO);
        return ResponseEntity.ok(user);
    }
    //Alterar usuário
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO user = alteraUserService.execute(id, userRequestDTO);
        return ResponseEntity.ok(user);
    }
    //Buscar usuário
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = buscarUserService.buscarTodos();
        return ResponseEntity.ok(users);
    }
    //Buscar usuário por ID
    @GetMapping("/id/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        UserResponseDTO user = buscarUserService.buscarPorId(id).orElse(null);
        return ResponseEntity.ok(user);
    }

    //Buscar usuário por username
    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponseDTO> getUserByUsername(@PathVariable String username) {
        UserResponseDTO user = buscarUserService.buscarPorUsername(username).orElse(null);
        return ResponseEntity.ok(user);
    }

    //Deleta usuário
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        deletarUserService.execute(id);
        return ResponseEntity.noContent().build();
    }
}
