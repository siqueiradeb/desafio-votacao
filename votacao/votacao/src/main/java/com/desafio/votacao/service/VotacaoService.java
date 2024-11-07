package com.desafio.votacao.service;

import com.desafio.votacao.model.CPFValidation;
import com.desafio.votacao.model.Pauta;
import com.desafio.votacao.model.Voto;
import com.desafio.votacao.repository.PautaRepository;
import com.desafio.votacao.repository.VotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class VotacaoService {

    @Autowired
    private PautaRepository pautaRepository;

    @Autowired
    private VotoRepository votoRepository;

    public Pauta criarPauta(String descricao) {
        Pauta pauta = new Pauta();
        pauta.setDescricao(descricao);
        pauta.setDataAbertura(LocalDateTime.now());
        pauta.setDataFechamento(LocalDateTime.now().plusMinutes(1)); // 1 minuto por padrão
        return pautaRepository.save(pauta);
    }

    public Voto votar(Long pautaId, String associadoId, String voto) {
        if (votoRepository.existsByPautaIdAndAssociadoId(pautaId, associadoId)) {
            throw new RuntimeException("Associado já votou nesta pauta.");
        }
        Pauta pauta = pautaRepository.findById(pautaId).orElseThrow(() -> new RuntimeException("Pauta não encontrada."));
        Voto novoVoto = new Voto();
        novoVoto.setPauta(pauta);
        novoVoto.setAssociadoId(associadoId);
        novoVoto.setVoto(voto);
        return votoRepository.save(novoVoto);
    }

    public String contarVotos(Long pautaId) {
        long votosSim = votoRepository.countByPautaIdAndVoto(pautaId, "Sim");
        long votosNao = votoRepository.countByPautaIdAndVoto(pautaId, "Não");
        return String.format("Votos Sim: %d, Votos Não: %d", votosSim, votosNao);
    }

    public CPFValidation validarCPF(String cpf) {
        return CPFValidation.validate(cpf);
    }
}
