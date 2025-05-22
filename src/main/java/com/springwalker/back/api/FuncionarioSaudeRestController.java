package com.springwalker.back.api;

import com.springwalker.back.model.FuncionarioSaude;
import com.springwalker.back.repository.FuncionarioSaudeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/funcionarioSaude")
public class FuncionarioSaudeRestController {

    @Autowired
    private FuncionarioSaudeRepository funcionarioSaudeRepository;

    @GetMapping("/listar")
    public List<FuncionarioSaude> listar(){
        return funcionarioSaudeRepository.findAll();
    }

    @PostMapping("/inserir")
    public void inserir(@RequestBody FuncionarioSaude funcionarioSaude){
        funcionarioSaudeRepository.save(funcionarioSaude);
    }

    @PatchMapping("/alterar")
    public void alterar(@RequestBody FuncionarioSaude funcionarioSaude){
        funcionarioSaudeRepository.save(funcionarioSaude);
    }

    @DeleteMapping("/excluir/{id}")
    public void excluir(@PathVariable Long id){
        funcionarioSaudeRepository.deleteById(id);
    }

    //Inserir Vários
    @PostMapping("/inserir-varios")
    public void inserirVarios(@RequestBody List<FuncionarioSaude> funcionarioSaudes){
        funcionarioSaudeRepository.saveAll(funcionarioSaudes);
    }

    // Buscar por Id
    @GetMapping("/buscar/{id}")
    public FuncionarioSaude buscarPorId(@PathVariable Long id){
        return funcionarioSaudeRepository.findById(id).get();
    }


    //Buscar por Nome
    @GetMapping("/buscar-por-nome/{nome}")
    public List<FuncionarioSaude> buscarPorNome(@PathVariable String nome){
        return funcionarioSaudeRepository.findFuncionarioSaudesByNomeContaining((nome));
    }



    //Buscar por Nome ou CPF
    //@GetMapping("/buscar-por-nome-ou-cpf/{nome}/{cpf}")
    //public List<FuncionarioSaude> buscarPorNomeOuCpf(
     //       @PathVariable String nome,
       //     @PathVariable String cpf
    //){
      //  return funcionarioSaudeRepository
        //        .findFuncionarioSaudesByNomeContainingOrCpfContaining(nome, cpf);
    //}




}
