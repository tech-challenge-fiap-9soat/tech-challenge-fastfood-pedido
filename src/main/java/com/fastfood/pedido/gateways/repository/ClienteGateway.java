package com.fastfood.pedido.gateways.repository;

import com.fastfood.pedido.domain.entities.ClienteEntity;

import java.util.Optional;

public interface ClienteGateway {
    Optional<ClienteEntity> findByCpf(String cpf);
    ClienteEntity save(ClienteEntity cliente);
}
