package com.fastfood.pedido.infrastructure.dto;

import com.fastfood.pedido.infrastructure.enums.StatusPedido;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusPedidoDTO {
    private Long id;
    private String cpf;
    private StatusPedido statusPedido;
    private Double valorTotal;
}