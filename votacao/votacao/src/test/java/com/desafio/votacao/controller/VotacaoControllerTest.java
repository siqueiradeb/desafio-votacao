package com.desafio.votacao.controller;

import com.desafio.votacao.dto.PautaDTO;
import com.desafio.votacao.model.Pauta;
import com.desafio.votacao.service.VotacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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


}
