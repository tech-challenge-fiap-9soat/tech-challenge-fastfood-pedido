package com.fastfood.pedido.gateways.repository.impl;

import com.fastfood.pedido.domain.entities.ProdutoEntity;
import com.fastfood.pedido.infrastructure.enums.CategoriaProduto;
import com.fastfood.pedido.infrastructure.repository.JpaProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoRepositoryGatewayTest {

    @Mock
    private JpaProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoRepositoryGateway produtoRepositoryGateway;

    private ProdutoEntity produto;

    @BeforeEach
    void setUp() {
        produto = new ProdutoEntity();
        produto.setId(1L);
        produto.setCategoria(CategoriaProduto.BEBIDA);
    }

    @Test
    void findAllByCategoria_deveDelegarParaRepositorio() {
        when(produtoRepository.findAllByCategoria(CategoriaProduto.BEBIDA))
                .thenReturn(List.of(produto));

        List<ProdutoEntity> resultado = produtoRepositoryGateway.findAllByCategoria(CategoriaProduto.BEBIDA);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(produto, resultado.get(0));
        verify(produtoRepository).findAllByCategoria(CategoriaProduto.BEBIDA);
    }

    @Test
    void findAllByIdIn_deveDelegarParaRepositorio() {
        List<Long> ids = List.of(1L, 2L);

        when(produtoRepository.findAllByIdIn(ids)).thenReturn(List.of(produto));

        List<ProdutoEntity> resultado = produtoRepositoryGateway.findAllByIdIn(ids);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(produto, resultado.get(0));
        verify(produtoRepository).findAllByIdIn(ids);
    }

    @Test
    void save_deveDelegarParaRepositorio() {
        when(produtoRepository.save(produto)).thenReturn(produto);

        ProdutoEntity resultado = produtoRepositoryGateway.save(produto);

        assertNotNull(resultado);
        assertEquals(produto, resultado);
        verify(produtoRepository).save(produto);
    }

    @Test
    void deleteById_deveDelegarParaRepositorio() {
        doNothing().when(produtoRepository).deleteById(1L);

        produtoRepositoryGateway.deleteById(1L);

        verify(produtoRepository).deleteById(1L);
    }
}
