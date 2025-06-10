package com.springwalker.back.api;

import com.springwalker.back.model.FuncionarioSaude;
import com.springwalker.back.model.Paciente;
import com.springwalker.back.repository.PacienteRepository;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/paciente")

public class PacienteRestController {
    @Autowired
    private PacienteRepository pacienteRepository;

    @GetMapping
    public List<Paciente> listar(){
        return pacienteRepository.findAll();
    }

    @PostMapping
    public void inserir(@RequestBody Paciente paciente){
        pacienteRepository.save(paciente);
    }


    @PutMapping("/{id}")
    public void alterar(@PathVariable Long id, @RequestBody Paciente paciente){

        Paciente pacienteExistente = pacienteRepository.findById(id).orElse(null);

        if(paciente.getCpf() != null){
            pacienteExistente.setCpf(paciente.getCpf());
        }

        if(paciente.getEmail() != null){
            pacienteExistente.setEmail(paciente.getEmail());
        }

        if(paciente.getNome() != null){
            pacienteExistente.setNome(paciente.getNome());
        }

        if(paciente.getDataNascimento() != null){
            pacienteExistente.setDataNascimento(paciente.getDataNascimento());
        }

        if(paciente.getTelefones() != null){
            pacienteExistente.setTelefones(paciente.getTelefones());
        }
        if (paciente.getAlergias() != null){
            pacienteExistente.setAlergias(paciente.getAlergias());
        }
        if (paciente.getCondicoes_preexistentes() != null){
            pacienteExistente.setCondicoes_preexistentes(paciente.getCondicoes_preexistentes());
        }

        if (paciente.getMedicoResponsavel() != null){
            pacienteExistente.setMedicoResponsavel(paciente.getMedicoResponsavel());
        }

        if (paciente.getQuarto() != null){
            pacienteExistente.setQuarto(paciente.getQuarto());
        }

        if (paciente.getDataInternacao() == null){
            pacienteExistente.setDataInternacao(paciente.getDataInternacao());
        }

        if (paciente.getHistorico() != null){
            pacienteExistente.setHistorico(paciente.getHistorico());
        }

        if (paciente.getLeito() != null){
            pacienteExistente.setLeito(paciente.getLeito());

        }

        pacienteRepository.save(pacienteExistente);
    }

    @DeleteMapping("/{id}")
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


    //Buscar por Cpf
    @GetMapping("/buscar-por-cpf/{cpf}")
    public List<Paciente> buscarPorCpf(@PathVariable String cpf){
        return pacienteRepository.findPacientesByCpfContaining(cpf);
    }

//    //Buscar por Nome ou CPF
//    @GetMapping("/buscar-por-nome-ou-cpf/{texto}")
//    public List<Paciente> buscarPorNomeOuCpf(
//            @PathVariable String texto
//    ){
//        return pacienteRepository
//                .buscarPacientePorNomeOuCpf(texto);
//    }


}
