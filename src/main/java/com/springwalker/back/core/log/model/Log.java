package com.springwalker.back.core.log.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "log")
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime timestamp;

    private String eventType;

    private Long userId;
    @Column(columnDefinition = "TEXT")
    private String description;

    // Construtor adicionado para facilitar o logging
    public Log(String description) {
        this.timestamp = LocalDateTime.now();
        this.eventType = "ACTION"; // Ou outro tipo padrão, se preferir
        this.description = description;
    }

    // Construtor atualizado para incluir userId
    public Log(String eventType, Long userId, String description) {
        this.timestamp = LocalDateTime.now();
        this.eventType = eventType;
        this.userId = userId;
        this.description = description;
    }
}
