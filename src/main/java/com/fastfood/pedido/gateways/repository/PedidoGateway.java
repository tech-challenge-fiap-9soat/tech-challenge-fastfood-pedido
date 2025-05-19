package com.fastfood.pedido.gateways.repository;

import com.fastfood.pedido.domain.entities.PedidoEntity;

import java.util.List;
import java.util.Optional;

public interface PedidoGateway {

    List<PedidoEntity> findAllToDisplay();

    Optional<PedidoEntity> findById(Long id);

    PedidoEntity save(PedidoEntity pedidoEntity);

}
