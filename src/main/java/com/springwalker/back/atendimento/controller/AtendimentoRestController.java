package com.springwalker.back.atendimento.controller;

import com.springwalker.back.atendimento.model.Atendimento;
import com.springwalker.back.atendimento.service.AlteraAtendimentoService;
import com.springwalker.back.atendimento.service.BuscaAtendimentoService;
import com.springwalker.back.atendimento.service.CriaAtendimentoService;
import com.springwalker.back.atendimento.service.DeletaAtendimentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/atendimento")
@CrossOrigin(origins = "*")
public class AtendimentoRestController {


    private final AlteraAtendimentoService alteraAtendimentos;
    private final CriaAtendimentoService criaAtendimento;
    private final BuscaAtendimentoService buscaAtendimento;
    private final DeletaAtendimentoService  deletaAtendimento;

    // Inserir um novo atendimento
    @PostMapping
    public ResponseEntity<Atendimento> criaAtendimento(@Valid @RequestBody Atendimento atendimento) {
        try {
            Atendimento novoAtendimento = criaAtendimento.criarAtendimento(atendimento);
            return new ResponseEntity<>(novoAtendimento, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // Lida com erros de validação de negócio (ex: médico responsável com cargo incorreto)
            return ResponseEntity.badRequest().body(null);
        } catch (NoSuchElementException e) {
            // Lida com o caso de IDs não encontrados (ex: médico responsável)
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            // Adicionado: Lida com a regra de negócio de atendimento em aberto,
            // retornando um status 409 Conflict.
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    // Buscar atendimento por Id
    @GetMapping("/{id}")
    public ResponseEntity<Atendimento> buscarAtendimentoPorId(@PathVariable Long id) {
        return buscaAtendimento.buscarAtendimentoPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Buscar todos atendimentos
    @GetMapping
    public ResponseEntity<List<Atendimento>> buscarTodosAtendimentos() {
        List<Atendimento> atendimentos = buscaAtendimento.buscarTodosAtendimentos();
        return ResponseEntity.ok(atendimentos);
    }

    // Alterar um atendimento existente
    @PutMapping("/{id}")
    public ResponseEntity<Atendimento> alterarAtendimento(@PathVariable Long id, @Valid @RequestBody Atendimento atendimentoAtualizado) {
        try {
            Atendimento atendimentoAlterado = alteraAtendimento.alterarAtendimento(id, atendimentoAtualizado);
            return ResponseEntity.ok(atendimentoAlterado);
        } catch (NoSuchElementException e) {
            // Lida com o caso em que o atendimento a ser alterado não foi encontrado.
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            // Lida com erros de validação de negócio (ex: médico responsável com cargo incorreto).
            return ResponseEntity.badRequest().build();
        }
    }

    // Apagar um atendimento
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAtendimento(@PathVariable Long id) {
        try {
            // O serviço agora não recebe um corpo e não retorna nada.
            deletaAtendimento.deletarAtendimento(id);
            // Retorna 204 No Content para indicar sucesso na deleção sem corpo na resposta.
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            // Lida com o caso em que o atendimento a ser deletado não foi encontrado.
            return ResponseEntity.notFound().build();
        }
    }
}
