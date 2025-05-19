package com.fastfood.pedido.infrastructure.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fastfood.pedido.domain.entities.ProdutoEntity;
import com.fastfood.pedido.infrastructure.dto.ProdutoDTO;
import com.fastfood.pedido.infrastructure.enums.CategoriaProduto;
import com.fastfood.pedido.usecases.produto.ProdutoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProdutoController.class)
@Import(ProdutoControllerTest.ProdutoServiceMockConfig.class)
class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class ProdutoServiceMockConfig {
        @Bean
        public ProdutoService produtoService() {
            return mock(ProdutoService.class);
        }
    }

    @Test
    void listaProdutosCategoria_deveRetornarListaDeProdutos() throws Exception {
        ProdutoEntity produto = new ProdutoEntity();
        produto.setId(1L);
        produto.setNome("Coca-Cola");

        when(produtoService.listarTodosDaCategoria(CategoriaProduto.BEBIDA))
                .thenReturn(List.of(produto));

        mockMvc.perform(get("/produto/categoria/BEBIDA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("Coca-Cola"));
    }

    @Test
    void cadastrarProduto_deveRetornarProdutoCriado() throws Exception {
        ProdutoDTO dto = new ProdutoDTO("Hamburguer","Delicioso",15.0,CategoriaProduto.LANCHE);

        ProdutoEntity produtoSalvo = new ProdutoEntity();
        produtoSalvo.setId(1L);

        when(produtoService.cadastrarProduto(any())).thenReturn(Optional.of(produtoSalvo));

        mockMvc.perform(post("/produto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void cadastrarProduto_deveRetornarNotFoundSeFalhar() throws Exception {
        ProdutoDTO dto = new ProdutoDTO("Item inválido","", null, null);

        when(produtoService.cadastrarProduto(any())).thenReturn(Optional.empty());

        mockMvc.perform(post("/produto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void editarProduto_deveRetornarProdutoEditado() throws Exception {
        ProdutoDTO dto = new ProdutoDTO("Editado", "", 20.0, CategoriaProduto.LANCHE);

        ProdutoEntity produtoEditado = new ProdutoEntity();
        produtoEditado.setId(10L);

        when(produtoService.editarProduto(eq(10L), any())).thenReturn(Optional.of(produtoEditado));

        mockMvc.perform(put("/produto/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L));
    }

    @Test
    void editarProduto_deveRetornarNotFoundSeNaoEncontrado() throws Exception {
        ProdutoDTO dto = new ProdutoDTO("", "", 0.0, null);
        when(produtoService.editarProduto(eq(123L), any())).thenReturn(Optional.empty());

        mockMvc.perform(put("/produto/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletarProduto_deveRetornarOk() throws Exception {
        doNothing().when(produtoService).deletarProduto(1L);

        mockMvc.perform(delete("/produto/1"))
                .andExpect(status().isOk());

        verify(produtoService).deletarProduto(1L);
    }
}
