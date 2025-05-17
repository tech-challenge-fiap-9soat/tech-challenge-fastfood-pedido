package com.fastfood.pedido.usecases.cliente.impl;

import com.fastfood.pedido.domain.entities.ClienteEntity;
import com.fastfood.pedido.gateways.repository.ClienteGateway;
import com.fastfood.pedido.usecases.cliente.BuscaClientePorCpf;
import com.fastfood.pedido.utils.CpfUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BuscaClientePorCpfImpl implements BuscaClientePorCpf {

    @Autowired
    private ClienteGateway clienteGateway;

    public Optional<ClienteEntity> buscaPorCpf(String cpf) {
        return clienteGateway.findByCpf(CpfUtils.removerPontuacoes(cpf));
    }
}
