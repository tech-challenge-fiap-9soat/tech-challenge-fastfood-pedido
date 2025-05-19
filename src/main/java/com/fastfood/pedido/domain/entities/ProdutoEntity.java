package com.fastfood.pedido.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fastfood.pedido.infrastructure.enums.CategoriaProduto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "produto")
public class ProdutoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private Double preco;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaProduto categoria;

    @ManyToMany(mappedBy = "produtos")
    @JsonIgnore
    private List<PedidoEntity> pedidosEntity;

}
