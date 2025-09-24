package com.springwalker.back.monitoramento.repository;

import com.springwalker.back.core.enums.StatusNotificacao;
import com.springwalker.back.monitoramento.model.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {

    List<Notificacao> findByStatus(StatusNotificacao status);

    List<Notificacao> findAllByOrderByDataCriacaoDesc();
}
