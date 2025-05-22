package com.springwalker.back.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@DiscriminatorValue(value = "F")
@Entity
public class FuncionarioSaude extends Pessoa {
}
