package com.desafio.votacao.model;

import lombok.Data;

import jakarta.persistence.*;

@Entity
@Data
public class Voto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Pauta pauta;

    private String associadoId; 
    private String voto; 
}
