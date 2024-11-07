package com.desafio.votacao.controller;

import com.desafio.votacao.dto.PautaDTO;
import com.desafio.votacao.exception.PautaException; // Usando PautaException
import com.desafio.votacao.exception.VotoException; // Usando VotoException
import com.desafio.votacao.model.Pauta;
import com.desafio.votacao.model.Voto;
import com.desafio.votacao.service.VotacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class VotacaoControllerTest {

    @InjectMocks
    private VotacaoController votacaoController;

    @Mock
    private VotacaoService votacaoService;

    private PautaDTO pautaDTO;
    private Pauta pauta;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); 
        pautaDTO = new PautaDTO(); 
        pautaDTO.setDescricao("Pauta Teste");

        pauta = new Pauta(); 
        pauta.setId(1L);
        pauta.setDescricao("Pauta Teste");
    }

    @Test
    public void testCriarPauta() {
        when(votacaoService.criarPauta("Pauta Teste")).thenReturn(pauta);

        ResponseEntity<Pauta> response = votacaoController.criarPauta(pautaDTO); 
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Pauta Teste", response.getBody().getDescricao());
    }

    @Test
    public void testAbrirVotacao_PautaNaoEncontrada() {
        Long pautaId = 1L;
        Map<String, Object> request = new HashMap<>();
        request.put("tempo", 1);

        when(votacaoService.pautaExists(pautaId)).thenReturn(false);

        Exception exception = assertThrows(PautaException.class, () -> {
            votacaoController.abrirVotacao(pautaId, request);
        });

        assertEquals("Pauta não encontrada.", exception.getMessage());
    }

    @Test
    public void testVotar_jaVotado() {
        Long pautaId = 1L;
        Voto voto = new Voto();
        voto.setAssociadoId("12345");
        voto.setVoto("Sim");

        when(votacaoService.votoJaRegistrado(pautaId, voto.getAssociadoId())).thenReturn(true);

        Exception exception = assertThrows(VotoException.class, () -> {
            votacaoController.votar(pautaId, voto);
        });

        assertEquals("O associado já votou nesta pauta.", exception.getMessage());
    }

    @Test
    public void testContarVotos_PautaNaoEncontrada() {
        Long pautaId = 1L;

        when(votacaoService.pautaExists(pautaId)).thenReturn(false);

        Exception exception = assertThrows(PautaException.class, () -> {
            votacaoController.contarVotos(pautaId);
        });

        assertEquals("Pauta não encontrada.", exception.getMessage());
    }
}
