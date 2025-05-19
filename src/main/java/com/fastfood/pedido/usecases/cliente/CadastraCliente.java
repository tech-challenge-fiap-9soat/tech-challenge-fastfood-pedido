package com.fastfood.pedido.usecases.cliente;

import com.fastfood.pedido.domain.entities.ClienteEntity;
import com.fastfood.pedido.infrastructure.dto.CadastroClienteDTO;

import java.util.Optional;

public interface CadastraCliente {
    Optional<ClienteEntity> cadastrarCliente(CadastroClienteDTO clienteDTO);
}
