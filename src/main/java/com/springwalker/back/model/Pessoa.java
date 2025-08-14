package com.springwalker.back.model;

import com.springwalker.back.enums.Sexo;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
        name = "tipo",
        length = 1,
        discriminatorType = DiscriminatorType.STRING
)
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
    @Column(unique = true) // Garante que o CPF seja único
    private String cpf; // Propriedade CPF

    public void addTelefone(Telefone telefone) {
        this.telefones.add(telefone);
    }


}
