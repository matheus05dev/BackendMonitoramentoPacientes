package com.springwalker.back.monitoramento.repository;

import com.springwalker.back.monitoramento.model.LeituraSensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeituraSensorRepository extends JpaRepository<LeituraSensor, Long> {
}
