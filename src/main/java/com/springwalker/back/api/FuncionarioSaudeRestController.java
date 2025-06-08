package com.springwalker.back.api;

import com.springwalker.back.model.FuncionarioSaude;
import com.springwalker.back.model.Paciente;
import com.springwalker.back.repository.FuncionarioSaudeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/funcionario")
@CrossOrigin(origins = "*")

public class FuncionarioSaudeRestController {
    @Autowired
    private FuncionarioSaudeRepository funcionarioSaudeRepository;

    @GetMapping
    public List<FuncionarioSaude> listar(){
        return funcionarioSaudeRepository.findAll();
    }

    @PostMapping
    public void inserir(@RequestBody FuncionarioSaude funcionarioSaude){
        funcionarioSaudeRepository.save(funcionarioSaude);
    }

    @PatchMapping("")
    public void alterar(@RequestBody FuncionarioSaude funcionarioSaude){
        funcionarioSaudeRepository.save(funcionarioSaude);
    }


    @PutMapping("/{id}")
    public void alterar(@PathVariable Long id, @RequestBody FuncionarioSaude funcionarioSaude){

        FuncionarioSaude funcionarioSaudeExistente = funcionarioSaudeRepository.findById(id).orElse(null);

        if(funcionarioSaude.getCpf() != null){
            funcionarioSaudeExistente.setCpf(funcionarioSaude.getCpf());
        }

        if(funcionarioSaude.getEmail() != null){
            funcionarioSaudeExistente.setEmail(funcionarioSaude.getEmail());
        }

        if(funcionarioSaude.getNome() != null){
            funcionarioSaudeExistente.setNome(funcionarioSaude.getNome());
        }

        if(funcionarioSaude.getDataNascimento() != null){
            funcionarioSaudeExistente.setDataNascimento(funcionarioSaude.getDataNascimento());
        }

        if(funcionarioSaude.getTelefones() != null){
            funcionarioSaudeExistente.setTelefones(funcionarioSaude.getTelefones());
        }

        if (funcionarioSaude.getCargo() != null) {
            funcionarioSaudeExistente.setCargo(funcionarioSaude.getCargo());
        }

        if (funcionarioSaude.getEspecialidades() != null) {
            funcionarioSaudeExistente.setEspecialidades(funcionarioSaude.getEspecialidades());
        }

        if (funcionarioSaude.getIndentificacao() != null) {
            funcionarioSaudeExistente.setIndentificacao(funcionarioSaude.getIndentificacao());
        }

        funcionarioSaudeRepository.save(funcionarioSaudeExistente);

    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id){
        funcionarioSaudeRepository.deleteById(id);
    }

    //Inserir Vários
    @PostMapping("/inserir-varios")
    public void inserirVarios(@RequestBody List<FuncionarioSaude> funcionarioSaudes){
        funcionarioSaudeRepository.saveAll(funcionarioSaudes);
    }

    // Buscar por Id
    @GetMapping("/{id}")
    public FuncionarioSaude buscarPorId(@PathVariable Long id){
        return funcionarioSaudeRepository.findById(id).get();
    }


    //Buscar por Nome
    @GetMapping("/buscar-por-nome/{nome}")
    public List<FuncionarioSaude> buscarPorNome(@PathVariable String nome){
        return funcionarioSaudeRepository.findFuncionarioSaudesByNomeContaining(nome);
    }

    //Buscar por Cpf
    @GetMapping("/buscar-por-cpf/{cpf}")
    public List<FuncionarioSaude> buscarPorCpf(@PathVariable String cpf){
        return funcionarioSaudeRepository.findFuncionarioSaudesByNomeContaining(cpf);
    }


//   // Buscar por Nome ou CPF
//    @GetMapping("/buscar-por-nome-ou-cpf/{texto}")
//    public List<FuncionarioSaude> buscarPorNomeOuCpf(
//            @PathVariable String texto
//    ){
//        return funcionarioSaudeRepository
//                .buscarFuncionarioSaudePorNomeOuCpf(texto);
//    }

}
