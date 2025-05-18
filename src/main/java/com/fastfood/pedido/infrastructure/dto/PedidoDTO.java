package com.fastfood.pedido.infrastructure.dto;

import com.fastfood.pedido.infrastructure.enums.StatusPedido;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class PedidoDTO {
    private String cpf;
    private StatusPedido statusPedido;
    private List<Long> produtosId;
}
