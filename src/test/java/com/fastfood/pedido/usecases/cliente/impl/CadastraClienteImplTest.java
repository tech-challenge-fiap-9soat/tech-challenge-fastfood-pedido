package com.fastfood.pedido.usecases.cliente.impl;

import com.fastfood.pedido.domain.entities.ClienteEntity;
import com.fastfood.pedido.gateways.repository.ClienteGateway;
import com.fastfood.pedido.infrastructure.dto.CadastroClienteDTO;
import com.fastfood.pedido.utils.CpfUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class CadastraClienteImplTest {

    @Mock
    private ClienteGateway clienteGateway;
    @InjectMocks
    private CadastraClienteImpl cadastraCliente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCadastrarClienteComDadosCorretos() {
        CadastroClienteDTO dto = new CadastroClienteDTO("joao@email.com","João","123.456.789-00");

        ClienteEntity clienteSalvo = new ClienteEntity();
        clienteSalvo.setNome(dto.getNome());
        clienteSalvo.setEmail(dto.getEmail());
        clienteSalvo.setCpf(CpfUtils.removerPontuacoes(dto.getCpf()));

        when(clienteGateway.save(any())).thenReturn(clienteSalvo);

        Optional<ClienteEntity> resultado = cadastraCliente.cadastrarCliente(dto);

        assertTrue(resultado.isPresent());
        ClienteEntity cliente = resultado.get();
        assertEquals("João", cliente.getNome());
        assertEquals("joao@email.com", cliente.getEmail());
        assertEquals("12345678900", cliente.getCpf());

        verify(clienteGateway).save(any(ClienteEntity.class));
    }
}
