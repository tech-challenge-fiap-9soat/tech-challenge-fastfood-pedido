package com.fastfood.pedido.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class PedidoDTO {
    private String cpf;
    private List<Long> produtosId;
}
