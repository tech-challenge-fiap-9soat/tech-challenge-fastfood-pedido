package com.fastfood.pedido.gateways.repository.impl;

import com.fastfood.pedido.domain.entities.PedidoEntity;
import com.fastfood.pedido.infrastructure.enums.StatusPedido;
import com.fastfood.pedido.infrastructure.repository.JpaPedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoRepositoryGatewayTest {

    @Mock
    private JpaPedidoRepository pedidoRepository;

    @InjectMocks
    private PedidoRepositoryGateway pedidoRepositoryGateway;

    private PedidoEntity pedido1;
    private PedidoEntity pedido2;

    @BeforeEach
    void setup() {
        pedido1 = new PedidoEntity();
        pedido1.setId(1L);
        pedido1.setStatusPedido(StatusPedido.RECEBIDO); // id 1
        pedido1.setCriadoEm(LocalDateTime.of(2023, 1, 10, 10, 0));

        pedido2 = new PedidoEntity();
        pedido2.setId(2L);
        pedido2.setStatusPedido(StatusPedido.PRONTO); // id 4
        pedido2.setCriadoEm(LocalDateTime.of(2023, 1, 11, 10, 0));
    }

    @Test
    void findAllToDisplay_deveRetornarListaOrdenada() {
        when(pedidoRepository.findAllToDisplay()).thenReturn(List.of(pedido1, pedido2));

        List<PedidoEntity> resultado = pedidoRepositoryGateway.findAllToDisplay();

        assertEquals(2, resultado.size());
        // Deve ordenar pelo statusPedido.id decrescente e criadoEm decrescente
        assertEquals(pedido2, resultado.get(0)); // Status id 4 é maior que 1
        assertEquals(pedido1, resultado.get(1));
    }

    @Test
    void findById_deveDelegarParaRepositorio() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido1));

        Optional<PedidoEntity> resultado = pedidoRepositoryGateway.findById(1L);

        assertTrue(resultado.isPresent());
        assertEquals(pedido1, resultado.get());
        verify(pedidoRepository).findById(1L);
    }

    @Test
    void save_deveDelegarParaRepositorio() {
        when(pedidoRepository.save(pedido1)).thenReturn(pedido1);

        PedidoEntity resultado = pedidoRepositoryGateway.save(pedido1);

        assertNotNull(resultado);
        assertEquals(pedido1, resultado);
        verify(pedidoRepository).save(pedido1);
    }
}
