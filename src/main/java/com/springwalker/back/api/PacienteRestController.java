package com.springwalker.back.api;

import com.springwalker.back.model.Paciente;
import com.springwalker.back.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/paciente")
public class PacienteRestController {
    @Autowired
    private PacienteRepository pacienteRepository;

    @GetMapping("/listar")
    public List<Paciente> listar(){
        return pacienteRepository.findAll();
    }

    @PostMapping("/inserir")
    public void inserir(@RequestBody Paciente paciente){
        pacienteRepository.save(paciente);
    }

    @PatchMapping("/alterar")
    public void alterar(@RequestBody Paciente paciente){
        pacienteRepository.save(paciente);
    }

    @DeleteMapping("/excluir/{id}")
    public void excluir(@PathVariable Long id){
        pacienteRepository.deleteById(id);
    }

    //Inserir Vários
    @PostMapping("/inserir-varios")
    public void inserirVarios(@RequestBody List<Paciente> pacientes){
        pacienteRepository.saveAll(pacientes);
    }

    // Buscar por Id
    @GetMapping("/buscar/{id}")
    public Paciente buscarPorId(@PathVariable Long id){
        return pacienteRepository.findById(id).get();
    }


    //Buscar por Nome
    @GetMapping("/buscar-por-nome/{nome}")
    public List<Paciente> buscarPorNome(@PathVariable String nome){
        return pacienteRepository.findPacientesByNomeContaining(nome);
    }



    //Buscar por Nome ou CPF
   // @GetMapping("/buscar-por-nome-ou-cpf/{nome}")
   // public List<Paciente> buscarPorNomeOuCpf(
   //         @PathVariable String nome
    //){
      //  return pacienteRepository
        //        .findPacientesByNomeContainingOrCpfContaining(nome);
    //}

}
