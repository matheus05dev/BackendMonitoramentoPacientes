package com.springwalker.back.controller;

import com.springwalker.back.model.FuncionarioSaude;
import com.springwalker.back.model.Telefone;
import com.springwalker.back.repository.FuncionarioSaudeRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/funcionario-saude")
public class FuncionarioSaudeController {

    @Autowired
    FuncionarioSaudeRepository funcionarioSaudeRepository;

    /*
     * Método que direciona para templates/funcionarioSaudes/listagem.html
     */
    @GetMapping
    public String listagem(Model model) {

        // Busca a lista de funcionarioSaudes no banco de dados
        List<FuncionarioSaude> listaFuncionarioSaudes = funcionarioSaudeRepository.findAll();

        // Adiciona a lista de funcionarioSaudes no objeto model para ser carregado no template
        model.addAttribute("funcionarioSaudes", listaFuncionarioSaudes);

        // Retorna o template funcionarioSaude/listagem.html
        return "funcionarioSaude/listagem";
    }

    /*
     * Método de acesso à página http://localhost:8080/funcionarioSaude/novo
     */
    @GetMapping("/form-inserir")
    public String formInserir(Model model){

        model.addAttribute("funcionarioSaude", new FuncionarioSaude());
        // templates/funcionarioSaude/inserir.html
        return "funcionarioSaude/inserir";
    }

    @PostMapping("/salvar")
    public String salvarFuncionarioSaude(
            @Valid FuncionarioSaude funcionarioSaude,
            BindingResult result,
            RedirectAttributes attributes) {

        // Se houver erro de validação, retorna para o template funcionarioSaudes/inserir.html
        if (result.hasErrors()){
            return "funcionarioSaude/inserir";
        }

        // Salva o funcionarioSaude no banco de dados
        funcionarioSaudeRepository.save(funcionarioSaude);

        // Adiciona uma mensagem que será exibida no template
        attributes.addFlashAttribute("mensagem", "FuncionarioSaude salvo com sucesso!");

        // Redireciona para a página de listagem de funcionarioSaudes
        return "redirect:/funcionarioSaude";
    }

    /*
     * Método para excluir um funcionarioSaude
     */
    @DeleteMapping("/excluir/{id}")
    public String excluir(@PathVariable("id") Long id,
                          RedirectAttributes attributes) {

        // Busca o funcionarioSaude no banco de dados
        FuncionarioSaude funcionarioSaude = funcionarioSaudeRepository.findById(id).orElseThrow(()
                -> new IllegalArgumentException("ID inválido"));

        // Exclui o funcionarioSaude do banco de dados
        funcionarioSaudeRepository.delete(funcionarioSaude);

        // Adiciona uma mensagem que será exibida no template
        attributes.addFlashAttribute("mensagem",
                "FuncionarioSaude excluído com sucesso!");

        // Redireciona para a página de listagem de funcionarioSaudes
        return "redirect:/funcionarioSaude";
    }


    /*
     * Método que direciona para templates/funcionarioSaudes/alterar.html
     */
    @GetMapping("/buscar/{id}")
    public String alterar(@PathVariable("id") Long id, Model model) {

        // Busca o funcionarioSaude no banco de dados
        FuncionarioSaude funcionarioSaude = funcionarioSaudeRepository.findById(id).orElseThrow(()
                -> new IllegalArgumentException("ID inválido"));

        // Adiciona o funcionarioSaude no objeto model para ser carregado no formulário
        model.addAttribute("funcionarioSaude", funcionarioSaude);

        // Retorna o template funcionarioSaude/alterar.html
        return "funcionarioSaude/alterar";
    }


    @PatchMapping("/alterar/{id}")
    public String alterar(@PathVariable("id") Long id,
                          @Valid FuncionarioSaude funcionarioSaude,
                          BindingResult result,
                          RedirectAttributes attributes) {

        // Se houver erro de validação, retorna para o template funcionarioSaudes/alterar.html
        if (result.hasErrors()) {
            return "funcionarioSaude/alterar";
        }

        // Busca o funcionarioSaude no banco de dados
        FuncionarioSaude funcionarioSaudeAtualizado = funcionarioSaudeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido"));



        // Seta os dados do funcionarioSaude

        funcionarioSaudeAtualizado.setNome(funcionarioSaude.getNome());
        funcionarioSaudeAtualizado.setEmail(funcionarioSaude.getEmail());

        // Salva o funcionarioSaude no banco de dados
        funcionarioSaudeRepository.save(funcionarioSaudeAtualizado);

        // Adiciona uma mensagem que será exibida no template
        attributes.addFlashAttribute("mensagem",
                "FuncionarioSaude atualizado com sucesso!");

        // Redireciona para a página de listagem de funcionarioSaudes
        return "redirect:/funcionarioSaude";
    }

    @PostMapping("/addTelefone")
    public String addTelefone(FuncionarioSaude funcionarioSaude) {
        funcionarioSaude.addTelefone(new Telefone());
        return "funcionarioSaude/inserir :: telefones";
    }

    @DeleteMapping("/removeTelefone")
    public String removeTelefone(FuncionarioSaude funcionarioSaude, @RequestParam("removeDynamicRow") Integer telefoneIndex) {
        funcionarioSaude.getTelefones().remove(telefoneIndex.intValue());
        return "funcionarioSaude/inserir :: telefones";
    }


}