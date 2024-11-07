package com.desafio.votacao.model;

import lombok.Data;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class Pauta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;

    private LocalDateTime dataAbertura;

    private LocalDateTime dataFechamento;

    @OneToMany(mappedBy = "pauta", cascade = CascadeType.ALL)
    private Set<Voto> votos = new HashSet<>();
}
