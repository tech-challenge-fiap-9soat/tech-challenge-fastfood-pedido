package com.fastfood.pedido.gateways.repository.impl;

import com.fastfood.pedido.domain.entities.ClienteEntity;
import com.fastfood.pedido.gateways.repository.ClienteGateway;
import com.fastfood.pedido.infrastructure.repository.JpaClienteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class ClienteRepositoryGateway implements ClienteGateway {

    private final JpaClienteRepository clienteRepository;

    @Override
    public Optional<ClienteEntity> findByCpf(String cpf) {
        return clienteRepository.findByCpf(cpf);
    }

    @Override
    public ClienteEntity save(ClienteEntity cliente) {
        return clienteRepository.save(cliente);
    }
}
