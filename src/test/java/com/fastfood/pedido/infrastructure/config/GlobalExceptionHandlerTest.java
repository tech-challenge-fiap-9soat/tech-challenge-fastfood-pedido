package com.fastfood.pedido.infrastructure.config;

import com.fastfood.pedido.domain.exception.BusinessException;
import com.fastfood.pedido.infrastructure.enums.ExceptionEnum;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void deveRetornarBadRequestComMensagemDaBusinessException() {
        BusinessException ex = new BusinessException(ExceptionEnum.PAGAMENTO_PENDENTE);

        ResponseEntity<String> response = handler.handlePagamentoPendenteException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ExceptionEnum.PAGAMENTO_PENDENTE.getMessage(), response.getBody());
    }
}
