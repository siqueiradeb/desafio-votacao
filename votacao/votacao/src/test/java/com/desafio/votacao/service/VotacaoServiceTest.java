package com.desafio.votacao.service;

import com.desafio.votacao.exception.PautaException; // Exceção para pauta não encontrada
import com.desafio.votacao.exception.VotoException; // Exceção para voto já registrado
import com.desafio.votacao.model.Pauta;
import com.desafio.votacao.model.Voto;
import com.desafio.votacao.repository.PautaRepository;
import com.desafio.votacao.repository.VotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class VotacaoServiceTest {

    @InjectMocks
    private VotacaoService votacaoService;

    @Mock
    private PautaRepository pautaRepository;

    @Mock
    private VotoRepository votoRepository;

    private Pauta pauta;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        pauta = new Pauta();
        pauta.setId(1L);
        pauta.setDescricao("Pauta Teste");
        pauta.setDataAbertura(LocalDateTime.now());
        pauta.setDataFechamento(LocalDateTime.now().plusMinutes(1));
    }

    @Test
    public void testCriarPauta() {
        when(pautaRepository.save(any(Pauta.class))).thenReturn(pauta);

        Pauta result = votacaoService.criarPauta("Pauta Teste");
        assertEquals("Pauta Teste", result.getDescricao());
        assertNotNull(result.getId());
    }

    @Test
    public void testVotar() {
        String associadoId = "12345";
        String voto = "Sim";

        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));
        when(votoRepository.existsByPautaIdAndAssociadoId(1L, associadoId)).thenReturn(false);
        when(votoRepository.save(any(Voto.class))).thenReturn(new Voto());

        Voto result = votacaoService.votar(1L, associadoId, voto);
        assertNotNull(result);
    }

    @Test
    public void testContarVotos() {
        when(votoRepository.countByPautaIdAndVoto(1L, "Sim")).thenReturn(10L);
        when(votoRepository.countByPautaIdAndVoto(1L, "Não")).thenReturn(5L);

        String resultado = votacaoService.contarVotos(1L);
        assertEquals("Votos Sim: 10, Votos Não: 5", resultado);
    }

    @Test
    public void testVotarJaVotado() {
        String associadoId = "12345";

        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));
        when(votoRepository.existsByPautaIdAndAssociadoId(1L, associadoId)).thenReturn(true);

        Exception exception = assertThrows(VotoException.class, () -> {
            votacaoService.votar(1L, associadoId, "Sim");
        });

        assertEquals("O associado já votou nesta pauta.", exception.getMessage());
    }

    @Test
    public void testVotarPautaNaoEncontrada() {
        String associadoId = "12345";

        when(pautaRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(PautaException.class, () -> {
            votacaoService.votar(1L, associadoId, "Sim");
        });

        assertEquals("Pauta não encontrada.", exception.getMessage());
    }
}
