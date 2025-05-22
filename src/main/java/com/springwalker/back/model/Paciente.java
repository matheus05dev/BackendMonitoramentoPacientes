package com.springwalker.back.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue(value = "P")
public class Paciente extends Pessoa {
}
