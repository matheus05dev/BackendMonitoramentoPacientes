package com.springwalker.back.monitoramento.repository;

import com.springwalker.back.monitoramento.enums.notificacao.StatusNotificacao;
import com.springwalker.back.monitoramento.enums.leitura.TipoDado;
import com.springwalker.back.monitoramento.model.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {

    List<Notificacao> findByStatus(StatusNotificacao status);

    List<Notificacao> findAllByOrderByDataCriacaoDesc();

    @Query("SELECT n FROM Notificacao n JOIN FETCH n.leituraSensor ls JOIN FETCH ls.atendimento ORDER BY n.dataCriacao DESC")
    List<Notificacao> findAllWithDetails();

    Optional<Notificacao> findFirstByLeituraSensorAtendimentoIdAndLeituraSensorTipoDadoAndStatusOrderByDataCriacaoDesc(
            Long atendimentoId, TipoDado tipoDado, StatusNotificacao status);
}
