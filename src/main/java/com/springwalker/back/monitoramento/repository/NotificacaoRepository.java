package com.springwalker.back.monitoramento.repository;

import com.springwalker.back.core.enums.StatusNotificacao;
import com.springwalker.back.core.enums.TipoDado;
import com.springwalker.back.monitoramento.model.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {

    List<Notificacao> findByStatus(StatusNotificacao status);

    List<Notificacao> findAllByOrderByDataCriacaoDesc();

    Optional<Notificacao> findFirstByLeituraSensorAtendimentoIdAndLeituraSensorTipoDadoAndStatusOrderByDataCriacaoDesc(
            Long atendimentoId, TipoDado tipoDado, StatusNotificacao status);
}
