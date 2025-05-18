package com.fastfood.pedido.domain.exception;

import com.fastfood.pedido.infrastructure.enums.ExceptionEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BusinessExceptionTest {

    @Test
    void deveCriarComMensagemFormatada() {
        ExceptionEnum mockEnum = mock(ExceptionEnum.class);
        when(mockEnum.getMessage()).thenReturn("Erro %s ocorreu");

        BusinessException exception = new BusinessException(mockEnum, "123");

        assertEquals("Erro 123 ocorreu", exception.getMessage());
        verify(mockEnum).getMessage();
    }

    @Test
    void deveCriarComMensagemSemParametros() {
        ExceptionEnum mockEnum = mock(ExceptionEnum.class);
        when(mockEnum.getMessage()).thenReturn("Erro padrão");

        BusinessException exception = new BusinessException(mockEnum);

        assertEquals("Erro padrão", exception.getMessage());
        verify(mockEnum).getMessage();
    }

    @Test
    void deveLancarECapturarExcecao() {
        ExceptionEnum mockEnum = mock(ExceptionEnum.class);
        when(mockEnum.getMessage()).thenReturn("Erro de teste");

        try {
            throw new BusinessException(mockEnum);
        } catch (BusinessException e) {
            assertEquals("Erro de teste", e.getMessage());
        }
    }
}
