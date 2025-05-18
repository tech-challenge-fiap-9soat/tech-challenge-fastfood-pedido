package com.fastfood.pedido.usecases.pedido;

import com.fastfood.pedido.domain.entities.PedidoEntity;
import com.fastfood.pedido.infrastructure.dto.PedidoDTO;

import java.util.List;
import java.util.Optional;

public interface PedidoService {

    List<PedidoEntity> listarTodos();

    Optional<PedidoEntity> checkoutPedido(PedidoDTO pedidoDTO);

    Optional<PedidoEntity> alterarPedido(Long id, PedidoDTO pedidoDTO);

    Optional<PedidoEntity> definirProximaOperacao(Long id);
}
