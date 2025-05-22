package com.springwalker.back.controller;

import com.springwalker.back.model.Paciente;
import com.springwalker.back.model.Telefone;
import com.springwalker.back.repository.PacienteRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/paciente")
public class PacienteController {

    @Autowired
    private PacienteRepository pacienteRepository;





    /*
     * Método que direciona para templates/pacientes/listagem.html
     */
    @GetMapping
    public String listagem(Model model) {

        // Busca a lista de pacientes no banco de dados
        List<Paciente> listaPacientes = pacienteRepository.findAll();

        // Adiciona a lista de pacientes no objeto model para ser carregado no template
        model.addAttribute("pacientees", listaPacientes);

        // Retorna o template paciente/listagem.html
        return "paciente/listagem";
    }

    @GetMapping("/buscar")
    public String buscar(Model model, @Param("nome") String nome) {
        if (nome == null) {
            return "redirect:/paciente";
        }
        List<Paciente> listaPacientes = pacienteRepository.findByNomeContainingIgnoreCase(nome);
        model.addAttribute("pacientees",listaPacientes);
        return "paciente/listagem";
    }


    /*
     * Método de acesso à página http://localhost:8080/paciente/novo
     */
    @PostMapping("/novo")
    public String cadastrar(Model model){

        // Adiciona um objeto paciente vazio para
        // ser carregado no formulário
        model.addAttribute("paciente", new Paciente());



        // Retorna o template paciente/inserir.html
        return "paciente/inserir";
    }

    @PostMapping("/salvar")
    public String salvarPaciente(@Valid Paciente paciente, BindingResult result,
                                 RedirectAttributes attributes) {

        // Se houver erro de validação, retorna para o template pacientes/inserir.html
        if (result.hasErrors()) {
            return "paciente/inserir";
        }



        // Salva o paciente no banco de dados
        pacienteRepository.save(paciente);

        // Adiciona uma mensagem que será exibida no template
        attributes.addFlashAttribute("mensagem", "Paciente salvo com sucesso!");

        // Redireciona para a página de listagem de pacientes
        return "redirect:/paciente/novo";
    }

    public BindingResult errosPersonalizadosInsercao(Paciente paciente, BindingResult result) {

        // Verifica se o e-mail já está cadastrado
        if (pacienteRepository.findByEmail(paciente.getEmail()) != null) {
            result.rejectValue("email", "email.existente",
                    "Já existe um paciente cadastrado com este e-mail");
        }


        return result;
    }







    /*
     * Método que direciona para templates/pacientes/alterar.html
     */
    @GetMapping("/buscar/{id}")
    public String alterar(@PathVariable("id") Long id, Model model) {

        // Busca o paciente no banco de dados
        Paciente paciente = pacienteRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido"));

        // Adiciona o paciente no objeto model para ser carregado no formulário
        model.addAttribute("paciente", paciente);

        // Retorna o template paciente/alterar.html
        return "paciente/alterar";
    }


    /*
     * Método que é invocado ao clicar no botão "Salvar" do template pacientes/alterar.html
     * O objeto paciente é carregado com os dados informados no formulário.
     * O objeto result contém o resultado da validação do formulário.
     * O objeto attributes é utilizado para enviar uma mensagem para o template.
     */
    @PatchMapping("/alterar/{id}")
    public String alterar(@PathVariable("id") Long id, @Valid Paciente paciente,
                          BindingResult result, RedirectAttributes attributes) {

        // Se houver erro de validação, retorna para o template pacientes/alterar.html
        if (result.hasErrors()) {
            return "paciente/alterar";
        }


        // Busca o paciente no banco de dados
        Paciente pacienteAtualizado = pacienteRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido"));


        // Seta os dados do paciente

        pacienteAtualizado.setNome(paciente.getNome());
        pacienteAtualizado.setEmail(paciente.getEmail());
        pacienteAtualizado.setTelefones(paciente.getTelefones());

        // Salva o paciente no banco de dados
        pacienteRepository.save(pacienteAtualizado);

        // Adiciona uma mensagem que será exibida no template
        attributes.addFlashAttribute("mensagem",
                "Paciente atualizado com sucesso!");

        // Redireciona para a página de listagem de pacientes
        return "redirect:/paciente";
    }

    /*
     * Método para excluir um paciente
     */
    @DeleteMapping("/excluir/{id}")
    public String excluir(@PathVariable("id") Long id,
                          RedirectAttributes attributes) {

        // Busca o paciente no banco de dados
        Paciente paciente = pacienteRepository.findById(id).orElseThrow(()
                -> new IllegalArgumentException("ID inválido"));

        // Exclui o paciente do banco de dados
        pacienteRepository.delete(paciente);

        // Adiciona uma mensagem que será exibida no template
        attributes.addFlashAttribute("mensagem",
                "Paciente excluído com sucesso!");

        // Redireciona para a página de listagem de pacientes
        return "redirect:/paciente";
    }

    @PostMapping("/addTelefone")
    public String addTelefone(Paciente paciente) {
        paciente.addTelefone(new Telefone());
        return "paciente/inserir :: telefones";
    }

    @DeleteMapping ("/removeTelefone")
    public String removeTelefone(Paciente paciente, @RequestParam("removeDynamicRow") Integer telefoneIndex) {
        paciente.getTelefones().remove(telefoneIndex.intValue());
        return "paciente/inserir :: telefones";
    }


}