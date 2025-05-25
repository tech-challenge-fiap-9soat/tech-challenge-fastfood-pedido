package com.fastfood.pedido.usecases.pedido.impl;

import com.fastfood.pedido.domain.entities.ClienteEntity;
import com.fastfood.pedido.domain.entities.PedidoEntity;
import com.fastfood.pedido.domain.entities.ProdutoEntity;
import com.fastfood.pedido.domain.exception.BusinessException;
import com.fastfood.pedido.gateways.http.PagamentoHttpClient;
import com.fastfood.pedido.gateways.http.ProducaoHttpClient;
import com.fastfood.pedido.gateways.repository.ClienteGateway;
import com.fastfood.pedido.gateways.repository.PedidoGateway;
import com.fastfood.pedido.gateways.repository.ProdutoGateway;
import com.fastfood.pedido.infrastructure.dto.PedidoDTO;
import com.fastfood.pedido.infrastructure.enums.StatusPedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PedidoServiceImplTest {

    @Mock
    private PedidoGateway pedidoGateway;
    @Mock
    private ProdutoGateway produtoGateway;
    @Mock
    private ClienteGateway clienteGateway;
    @Mock
    private PagamentoHttpClient pagamentoHttpClient;
    @Mock
    private ProducaoHttpClient producaoHttpClient;
    @InjectMocks
    private PedidoServiceImpl pedidoService;
    private PedidoEntity pedido;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        pedido = new PedidoEntity();
        pedido.setId(1L);
        pedido.setStatusPedido(StatusPedido.RECEBIDO);
    }

    @Test
    void listarTodos_deveChamarFindAllToDisplay() {
        pedidoService.listarTodos();
        verify(pedidoGateway).findAllToDisplay();
    }

    @Test
    void checkoutPedido_deveRetornarPedidoSalvo() {
        PedidoDTO dto = new PedidoDTO("12345678900", List.of(1L, 2L));

        ClienteEntity cliente = new ClienteEntity();
        ProdutoEntity produto1 = new ProdutoEntity();
        produto1.setPreco(10.0);
        ProdutoEntity produto2 = new ProdutoEntity();
        produto2.setPreco(15.0);

        when(clienteGateway.findByCpf(dto.getCpf())).thenReturn(Optional.of(cliente));
        when(produtoGateway.findAllByIdIn(dto.getProdutosId())).thenReturn(List.of(produto1, produto2));
        when(pedidoGateway.save(any())).thenAnswer(i -> i.getArgument(0));

        Optional<PedidoEntity> resultado = pedidoService.checkoutPedido(dto);

        assertTrue(resultado.isPresent());
        PedidoEntity pedidoSalvo = resultado.get();
        assertEquals(cliente, pedidoSalvo.getCliente());
        assertEquals(2, pedidoSalvo.getProdutos().size());
        assertEquals(25.0, pedidoSalvo.getValorTotal());
        assertEquals(StatusPedido.RECEBIDO, pedidoSalvo.getStatusPedido());
    }

    @Test
    void checkoutPedido_deveLancarExcecaoQuandoProdutosEstaoVazios() {
        PedidoDTO dto = new PedidoDTO("12345678900",List.of());
        dto.setProdutosId(List.of());

        assertThrows(BusinessException.class, () -> pedidoService.checkoutPedido(dto));
    }

    @Test
    void alterarPedido_deveAtualizarPedidoComSucesso() {
        Long id = 1L;
        PedidoDTO dto = new PedidoDTO("12345678900", List.of(1L));

        ProdutoEntity produto = new ProdutoEntity();
        produto.setPreco(20.0);

        PedidoEntity pedidoExistente = new PedidoEntity();
        pedidoExistente.setStatusPedido(StatusPedido.RECEBIDO);

        when(pedidoGateway.findById(id)).thenReturn(Optional.of(pedidoExistente));
        when(produtoGateway.findAllByIdIn(dto.getProdutosId())).thenReturn(List.of(produto));
        when(pedidoGateway.save(any())).thenAnswer(i -> i.getArgument(0));

        Optional<PedidoEntity> resultado = pedidoService.alterarPedido(id, dto);

        assertTrue(resultado.isPresent());
        PedidoEntity pedido = resultado.get();
        assertEquals(StatusPedido.RECEBIDO, pedido.getStatusPedido());
        assertEquals(20.0, pedido.getValorTotal());
        assertEquals(1, pedido.getProdutos().size());
    }

    @Test
    void alterarPedido_deveLancarExcecaoQuandoPedidoNaoExiste() {
        when(pedidoGateway.findById(1L)).thenReturn(Optional.empty());
        PedidoDTO dto = new PedidoDTO("12345678900", List.of());

        assertThrows(IllegalArgumentException.class, () -> pedidoService.alterarPedido(1L, dto));
    }

    @Test
    void alterarPedido_naoDeveAtualizarStatusSeNaoForPermitido() {
        Long id = 1L;
        PedidoDTO dto = new PedidoDTO("12345678900", List.of(1L));

        ProdutoEntity produto = new ProdutoEntity();
        produto.setPreco(10.0);

        PedidoEntity pedidoExistente = new PedidoEntity();
        pedidoExistente.setStatusPedido(StatusPedido.EM_PREPARACAO);

        when(pedidoGateway.findById(id)).thenReturn(Optional.of(pedidoExistente));
        when(produtoGateway.findAllByIdIn(dto.getProdutosId())).thenReturn(List.of(produto));
        when(pedidoGateway.save(any())).thenAnswer(i -> i.getArgument(0));

        Optional<PedidoEntity> resultado = pedidoService.alterarPedido(id, dto);

        assertEquals(StatusPedido.EM_PREPARACAO, resultado.get().getStatusPedido());
    }

    @Test
    void deveAvancarParaProximaOperacaoQuandoPagamentoAprovado() {
        PedidoEntity atualizado = new PedidoEntity();
        atualizado.setId(1L);
        atualizado.setStatusPedido(StatusPedido.EM_PREPARACAO);

        when(pedidoGateway.findById(1L)).thenReturn(Optional.of(pedido));
        when(pagamentoHttpClient.consultaPagamentoAprovado(1L)).thenReturn("Aprovado");
        when(pedidoGateway.save(any())).thenReturn(atualizado);

        Optional<PedidoEntity> resultado = pedidoService.definirProximaOperacao(1L);

        assertTrue(resultado.isPresent());
        assertEquals(StatusPedido.EM_PREPARACAO, resultado.get().getStatusPedido());

        verify(pedidoGateway).save(argThat(p -> p.getStatusPedido().equals(StatusPedido.EM_PREPARACAO)));
    }

    @Test
    void deveLancarBusinessExceptionQuandoPagamentoNaoAprovadoParaEmPreparacao() {
        when(pedidoGateway.findById(1L)).thenReturn(Optional.of(pedido));
        when(pagamentoHttpClient.consultaPagamentoAprovado(1L)).thenReturn("Reprovado");

        BusinessException ex = assertThrows(BusinessException.class, () -> pedidoService.definirProximaOperacao(1L));
        assertEquals("O pagamento do pedido 1 ainda está pendente.", ex.getMessage());

        verify(pedidoGateway, never()).save(any());
    }

    @Test
    void deveLancarIllegalArgumentExceptionQuandoPedidoNaoExiste() {
        when(pedidoGateway.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> pedidoService.definirProximaOperacao(99L));

        verify(pagamentoHttpClient, never()).consultaPagamentoAprovado(anyLong());
        verify(pedidoGateway, never()).save(any());
    }
}
