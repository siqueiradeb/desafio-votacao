package com.desafio.votacao.controller;

import com.desafio.votacao.dto.PautaDTO;
import com.desafio.votacao.exception.PautaException;
import com.desafio.votacao.exception.VotoException;
import com.desafio.votacao.model.CPFValidation;
import com.desafio.votacao.model.Pauta;
import com.desafio.votacao.model.Voto;
import com.desafio.votacao.service.VotacaoService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/votacao")
public class VotacaoController {

    @Autowired
    private VotacaoService votacaoService;

    @PostMapping("/pautas")
    public ResponseEntity<Pauta> criarPauta(@RequestBody PautaDTO pautaDTO) {
        Pauta novaPauta = votacaoService.criarPauta(pautaDTO.getDescricao());
        return ResponseEntity.ok(novaPauta);
    }

    @PostMapping("/pautas/{pautaId}/voto")
    public ResponseEntity<Voto> votar(@PathVariable Long pautaId, @RequestBody Voto voto) {
        if (votacaoService.votoJaRegistrado(pautaId, voto.getAssociadoId())) {
            throw new VotoException("O associado já votou nesta pauta.");
        }

        Voto novoVoto = votacaoService.votar(pautaId, voto.getAssociadoId(), voto.getVoto());
        return ResponseEntity.ok(novoVoto);
    }

    @PostMapping("/pautas/{pautaId}/abertura")
    public ResponseEntity<String> abrirVotacao(@PathVariable Long pautaId, @RequestBody Map<String, Object> request) {
        if (!votacaoService.pautaExists(pautaId)) {
            throw new PautaException("Pauta não encontrada.");
        }

        Integer tempo = request.containsKey("tempo") ? (Integer) request.get("tempo") : 1; 
        return ResponseEntity.ok("Sessão de votação aberta por " + tempo + " minuto(s).");
    }

    @GetMapping("/pautas/{pautaId}/resultados")
    public ResponseEntity<String> contarVotos(@PathVariable Long pautaId) {
        if (!votacaoService.pautaExists(pautaId)) {
            throw new PautaException("Pauta não encontrada.");
        }

        String resultado = votacaoService.contarVotos(pautaId);
        return ResponseEntity.ok(resultado);
    }

    @PostMapping("/cpf/validar")
    public ResponseEntity<CPFValidation> validarCpf(@RequestBody Map<String, String> cpfRequest) {
        String cpf = cpfRequest.get("cpf");
        CPFValidation validation = votacaoService.validarCPF(cpf);
        return ResponseEntity.ok(validation);
    }
}
