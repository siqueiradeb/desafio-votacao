package com.desafio.votacao.repository;

import com.desafio.votacao.model.Voto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotoRepository extends JpaRepository<Voto, Long> {
    boolean existsByPautaIdAndAssociadoId(Long pautaId, String associadoId);

    long countByPautaIdAndVoto(Long pautaId, String string);
}
