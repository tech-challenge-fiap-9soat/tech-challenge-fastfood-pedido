package com.fastfood.pedido.gateways.repository.impl;

import com.fastfood.pedido.domain.entities.ClienteEntity;
import com.fastfood.pedido.infrastructure.repository.JpaClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClienteRepositoryGatewayTest {

    @Mock
    private JpaClienteRepository clienteRepository;

    @InjectMocks
    private ClienteRepositoryGateway clienteRepositoryGateway;

    @Test
    void findByCpf_deveDelegarParaRepositorio() {
        String cpf = "12345678900";
        ClienteEntity cliente = new ClienteEntity();
        cliente.setCpf(cpf);

        when(clienteRepository.findByCpf(cpf)).thenReturn(Optional.of(cliente));

        Optional<ClienteEntity> resultado = clienteRepositoryGateway.findByCpf(cpf);

        assertTrue(resultado.isPresent());
        assertEquals(cpf, resultado.get().getCpf());
        verify(clienteRepository).findByCpf(cpf);
    }

    @Test
    void save_deveDelegarParaRepositorio() {
        ClienteEntity cliente = new ClienteEntity();
        cliente.setCpf("12345678900");

        when(clienteRepository.save(cliente)).thenReturn(cliente);

        ClienteEntity resultado = clienteRepositoryGateway.save(cliente);

        assertNotNull(resultado);
        assertEquals(cliente.getCpf(), resultado.getCpf());
        verify(clienteRepository).save(cliente);
    }
}
