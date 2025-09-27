package com.springwalker.back.monitoramento.model;

import com.springwalker.back.atendimento.model.Atendimento;
import com.springwalker.back.core.enums.CondicaoSaude;
import com.springwalker.back.core.enums.Gravidade;
import com.springwalker.back.core.enums.TipoDado;
import com.springwalker.back.core.enums.UnidadeMedida;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class LeituraSensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Column(name = "valor")
    private Double valor;

    @Column(name = "data_hora")
    private LocalDateTime dataHora;

    @Column(name = "tipo_dado")
    @Enumerated(EnumType.STRING)
    private TipoDado tipoDado;

    @Column(name = "unidade_medida")
    @Enumerated(EnumType.STRING)
    private UnidadeMedida unidadeMedida;

    @ManyToOne
    @JoinColumn(name = "atendimento_id", nullable = false)
    private Atendimento atendimento;

    @Column(name = "gravidade")
    @Enumerated(EnumType.STRING)
    private Gravidade gravidade;

    @Column(name = "condicao_saude")
    @Enumerated(EnumType.STRING)
    private CondicaoSaude condicaoSaude;

    @OneToOne(mappedBy = "leituraSensor", cascade = CascadeType.ALL, orphanRemoval = true)
    private Notificacao notificacao;
}
