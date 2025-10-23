package com.springwalker.back.pessoa.repository;

import com.springwalker.back.pessoa.model.Telefone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TelefoneRepository extends JpaRepository<Telefone, Long> {
}
