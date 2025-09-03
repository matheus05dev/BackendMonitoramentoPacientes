package com.springwalker.back.core.model;

import com.springwalker.back.core.enums.Sexo;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.aspectj.bridge.IMessage;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique=true)
    private Long id;

    @NotEmpty(message = "O nome deve ser informado")
    private String nome;

    @NotEmpty(message = "O e-mail deve ser informado")
    @Email(message = "O e-mail informado é inválido")
    @Column(unique = true)
    private String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "Sexo")
    private Sexo sexo;

    @NotNull
    @Temporal(TemporalType.DATE)
    @Basic
    @Column(name = "Data Nascimento")
    private LocalDate dataNascimento;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "Pessoa id")
    private List<Telefone> telefones = new ArrayList<Telefone>();

    @NotEmpty(message = "O CPF deve ser informado") // Validação para CPF
    @Column(name = "CPF", unique = true)// Garante que o CPF seja único
    @CPF (message = "O CPF informado é inválido")// Validação do CPF
    private String cpf; // Propriedade CPF

    public void addTelefone(Telefone telefone) {
        this.telefones.add(telefone);
    }

}
