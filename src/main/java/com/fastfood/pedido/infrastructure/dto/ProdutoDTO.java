package com.fastfood.pedido.infrastructure.dto;

import com.fastfood.pedido.infrastructure.enums.CategoriaProduto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProdutoDTO {

    private String nome;
    private String descricao;
    private Double preco;
    private CategoriaProduto categoria;
}
