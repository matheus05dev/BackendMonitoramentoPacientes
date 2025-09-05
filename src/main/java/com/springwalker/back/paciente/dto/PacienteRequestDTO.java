package com.springwalker.back.paciente.dto;

import com.springwalker.back.core.dto.TelefoneDTO;
import com.springwalker.back.core.enums.Sexo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;
import java.util.List;

@Data
public class PacienteRequestDTO {

    @NotEmpty(message = "O nome deve ser informado")
    private String nome;

    @NotEmpty(message = "O e-mail deve ser informado")
    @Email(message = "O e-mail informado é inválido")
    private String email;

    @NotNull
    private Sexo sexo;

    @NotNull
    private LocalDate dataNascimento;

    @NotEmpty(message = "O CPF deve ser informado")
    @CPF(message = "O CPF informado é inválido")
    private String cpf;

    private List<TelefoneDTO> telefones;

    private List<String> alergias;

    // Apenas o ID do quarto é recebido. O serviço se encarregará de associar a entidade.
    private Long quartoId;
}
