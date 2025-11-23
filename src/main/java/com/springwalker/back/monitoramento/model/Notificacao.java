package com.springwalker.back.monitoramento.model;

import com.springwalker.back.monitoramento.enums.notificacao.StatusNotificacao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "leitura_sensor_id", nullable = false)
    private LeituraSensor leituraSensor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusNotificacao status;

    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    private LocalDateTime dataFechamento;

    @Column(name = "numero_quarto")
    private Integer numeroQuarto;

}
