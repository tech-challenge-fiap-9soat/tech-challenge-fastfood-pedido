package com.fastfood.pedido.usecases.cliente;

import com.fastfood.pedido.domain.entities.ClienteEntity;

import java.util.Optional;

public interface BuscaClientePorCpf {
    Optional<ClienteEntity> buscaPorCpf(String cpf);
}
