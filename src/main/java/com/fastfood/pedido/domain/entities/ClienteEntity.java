package com.fastfood.pedido.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "cliente")
public class ClienteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Setter
    @Column(nullable = false)
    private String email;

    @Setter
    @Column(nullable = false)
    private String nome;

    @Setter
    @Column(nullable = false, unique = true)
    private String cpf;
}
