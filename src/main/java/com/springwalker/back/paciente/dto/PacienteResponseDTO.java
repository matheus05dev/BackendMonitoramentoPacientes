package com.springwalker.back.paciente.dto;

import com.springwalker.back.core.dto.TelefoneDTO;
import com.springwalker.back.core.enums.Sexo;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PacienteResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private Sexo sexo;
    private LocalDate dataNascimento;
    private String cpf;
    private List<TelefoneDTO> telefones;
    private List<String> alergias;
    private Long quartoId;
}
