package com.fastfood.pedido.usecases.cliente.impl;

import com.fastfood.pedido.domain.entities.ClienteEntity;
import com.fastfood.pedido.gateways.repository.ClienteGateway;
import com.fastfood.pedido.utils.CpfUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscaClientePorCpfImplTest {

    @Mock
    private ClienteGateway clienteGateway;

    @InjectMocks
    private BuscaClientePorCpfImpl buscaClientePorCpf;

    @Test
    void deveBuscarClienteComCpfSemPontuacao() {
        String cpfComPontuacao = "123.456.789-00";
        String cpfLimpo = CpfUtils.removerPontuacoes(cpfComPontuacao);

        ClienteEntity cliente = new ClienteEntity();
        cliente.setCpf(cpfLimpo);
        cliente.setNome("Carlos");

        when(clienteGateway.findByCpf(cpfLimpo)).thenReturn(Optional.of(cliente));

        Optional<ClienteEntity> resultado = buscaClientePorCpf.buscaPorCpf(cpfComPontuacao);

        assertTrue(resultado.isPresent());
        assertEquals("Carlos", resultado.get().getNome());
        assertEquals(cpfLimpo, resultado.get().getCpf());
        verify(clienteGateway).findByCpf(cpfLimpo);
    }

    @Test
    void deveRetornarEmptyQuandoClienteNaoEncontrado() {
        String cpf = "98765432100";
        when(clienteGateway.findByCpf(cpf)).thenReturn(Optional.empty());

        Optional<ClienteEntity> resultado = buscaClientePorCpf.buscaPorCpf(cpf);
        assertTrue(resultado.isEmpty());
        verify(clienteGateway).findByCpf(cpf);
    }
}
