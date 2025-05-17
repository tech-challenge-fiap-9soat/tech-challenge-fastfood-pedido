package com.fastfood.pedido.infrastructure.enums;

public enum CategoriaProduto {
    LANCHE(1,"Lanche"),
    ACOMPANHAMENTO(2,"Acompanhamento"),
    BEBIDA(3,"Bebida"),
    SOBREMESA(4,"Sobremesa");

    private int id;
    private String nome;

    CategoriaProduto(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }
}
