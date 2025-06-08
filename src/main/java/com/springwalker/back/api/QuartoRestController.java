package com.springwalker.back.api;

import com.springwalker.back.model.FuncionarioSaude;
import com.springwalker.back.model.Paciente;
import com.springwalker.back.model.Quarto;
import com.springwalker.back.repository.QuartoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quarto")
@CrossOrigin(origins = "*")

public class QuartoRestController {
    @Autowired
    private QuartoRepository quartoRepository;

    //Buscar
    @GetMapping
    public List<Quarto> listar(){
        return quartoRepository.findAll();
    }

    //Inserir
    @PostMapping
    public void inserir(@RequestBody Quarto quarto){
        quartoRepository.save(quarto);
    }

    //Alterar
    @PutMapping("/{id}")
    public void alterar(@PathVariable Long id, @RequestBody Quarto quarto){
        Quarto quartoExistente = quartoRepository.findById(id).orElse(null);

        if(quarto.getCapacidade() != null){
            quartoExistente.setCapacidade(quarto.getCapacidade());
        }

        if (quarto.getLocalizacao() != null){
            quartoExistente.setLocalizacao(quarto.getLocalizacao());
        }

        if (quarto.getNumero() != null){
            quartoExistente.setNumero(quarto.getNumero());
        }

        if (quarto.getTipo() != null){
            quartoExistente.setTipo(quarto.getTipo());
        }

        quartoRepository.save(quarto);
    }

    //Apagar
    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id){
        quartoRepository.deleteById(id);
    }

    //Inserir Vários
    @PostMapping("/inserir-varios")
    public void inserirVarios(@RequestBody List<Quarto> quartos){
        quartoRepository.saveAll(quartos);
    }
}
