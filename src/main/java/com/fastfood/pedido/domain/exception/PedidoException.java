package com.fastfood.pedido.domain.exception;

public class PedidoException extends RuntimeException{
    public PedidoException(Long pedidoId) {
        super("O pagamento do pedido " + pedidoId + " ainda está pendente.");
    }
}
