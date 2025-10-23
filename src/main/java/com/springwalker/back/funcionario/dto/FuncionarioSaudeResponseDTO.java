package com.springwalker.back.funcionario.dto;

import com.springwalker.back.pessoa.dto.TelefoneDTO;
import com.springwalker.back.funcionario.enums.Cargo;
import com.springwalker.back.pessoa.enums.Sexo;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class FuncionarioSaudeResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private Sexo sexo;
    private LocalDate dataNascimento;
    private String cpf;
    private List<TelefoneDTO> telefones;
    private Cargo cargo;
    private List<String> especialidades;
    private String identificacao;
    // IDs para evitar carga pesada e referências circulares
    private List<Long> atendimentosIds;
    private List<Long> atendimentosComplicacaoIds;
}
