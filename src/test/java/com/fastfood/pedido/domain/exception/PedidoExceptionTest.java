package com.fastfood.pedido.domain.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PedidoExceptionTest {

    @Test
    void deveCriarExcecaoComMensagemCorreta() {
        Long pedidoId = 42L;
        PedidoException exception = new PedidoException(pedidoId);

        assertEquals("O pagamento do pedido 42 ainda está pendente.", exception.getMessage());
    }

    @Test
    void deveLancarEPegarPedidoException() {
        Long pedidoId = 99L;

        try {
            throw new PedidoException(pedidoId);
        } catch (PedidoException e) {
            assertEquals("O pagamento do pedido 99 ainda está pendente.", e.getMessage());
        }
    }
}
