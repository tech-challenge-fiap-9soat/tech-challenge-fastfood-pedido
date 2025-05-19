package com.fastfood.pedido.usecases.produto.impl;

import com.fastfood.pedido.domain.entities.ProdutoEntity;
import com.fastfood.pedido.gateways.repository.ProdutoGateway;
import com.fastfood.pedido.infrastructure.dto.ProdutoDTO;
import com.fastfood.pedido.infrastructure.enums.CategoriaProduto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceImplTest {

    @Mock
    private ProdutoGateway produtoGateway;

    @InjectMocks
    private ProdutoServiceImpl produtoService;

    private ProdutoDTO produtoDTO;

    @BeforeEach
    void setUp() {
        produtoDTO = new ProdutoDTO("Coca-Cola","Refrigerante",5.0, CategoriaProduto.BEBIDA);
    }

    @Test
    void listarTodosDaCategoria_deveChamarGatewayECapturarResultado() {
        ProdutoEntity produto = new ProdutoEntity();
        produto.setNome("Sprite");

        when(produtoGateway.findAllByCategoria(CategoriaProduto.BEBIDA)).thenReturn(List.of(produto));

        List<ProdutoEntity> resultado = produtoService.listarTodosDaCategoria(CategoriaProduto.BEBIDA);

        assertEquals(1, resultado.size());
        assertEquals("Sprite", resultado.get(0).getNome());
        verify(produtoGateway).findAllByCategoria(CategoriaProduto.BEBIDA);
    }

    @Test
    void cadastrarProduto_deveConverterDTOESalvar() {
        ArgumentCaptor<ProdutoEntity> captor = ArgumentCaptor.forClass(ProdutoEntity.class);
        ProdutoEntity salvo = new ProdutoEntity();
        salvo.setId(1L);

        when(produtoGateway.save(any())).thenReturn(salvo);

        Optional<ProdutoEntity> resultado = produtoService.cadastrarProduto(produtoDTO);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        verify(produtoGateway).save(captor.capture());

        ProdutoEntity capturado = captor.getValue();
        assertEquals("Coca-Cola", capturado.getNome());
        assertEquals("Refrigerante", capturado.getDescricao());
        assertEquals(5.0, capturado.getPreco());
        assertEquals(CategoriaProduto.BEBIDA, capturado.getCategoria());
    }

    @Test
    void editarProduto_deveAtualizarComIdCorreto() {
        ProdutoEntity salvo = new ProdutoEntity();
        salvo.setId(99L);

        when(produtoGateway.save(any())).thenReturn(salvo);

        Optional<ProdutoEntity> resultado = produtoService.editarProduto(99L, produtoDTO);

        assertTrue(resultado.isPresent());
        assertEquals(99L, resultado.get().getId());

        ArgumentCaptor<ProdutoEntity> captor = ArgumentCaptor.forClass(ProdutoEntity.class);
        verify(produtoGateway).save(captor.capture());
        assertEquals(99L, captor.getValue().getId());
    }

    @Test
    void deletarProduto_deveChamarGateway() {
        doNothing().when(produtoGateway).deleteById(10L);

        produtoService.deletarProduto(10L);

        verify(produtoGateway).deleteById(10L);
    }
}
