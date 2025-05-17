package com.fastfood.pedido.infrastructure.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fastfood.pedido.domain.entities.PedidoEntity;
import com.fastfood.pedido.infrastructure.dto.PedidoDTO;
import com.fastfood.pedido.infrastructure.enums.StatusPedido;
import com.fastfood.pedido.usecases.pedido.PedidoService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PedidoController.class)
@Import(PedidoControllerTest.PedidoServiceConfig.class)
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class PedidoServiceConfig {
        @Bean
        public PedidoService pedidoService() {
            return mock(PedidoService.class);
        }
    }

    @Test
    void listaPedidos_deveRetornarListaDePedidos() throws Exception {
        PedidoEntity pedido = new PedidoEntity();
        pedido.setId(1L);

        when(pedidoService.listarTodos()).thenReturn(List.of(pedido));

        mockMvc.perform(get("/pedido"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void cadastrarPedido_deveRetornarPedidoCriado() throws Exception {
        PedidoDTO dto = new PedidoDTO("12345678900", StatusPedido.EM_PREPARACAO, List.of(1L));
        dto.setCpf("12345678900");
        dto.setProdutosId(List.of(1L));

        PedidoEntity pedidoSalvo = new PedidoEntity();
        pedidoSalvo.setId(1L);

        when(pedidoService.checkoutPedido(any())).thenReturn(Optional.of(pedidoSalvo));

        mockMvc.perform(post("/pedido")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void cadastrarPedido_deveRetornarNotFoundSeFalhar() throws Exception {
        PedidoDTO dto = new PedidoDTO("12345678900", null, List.of(1L));

        when(pedidoService.checkoutPedido(any())).thenReturn(Optional.empty());

        mockMvc.perform(post("/pedido")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void alterarPedido_deveRetornarPedidoAtualizado() throws Exception {
        PedidoDTO dto = new PedidoDTO("12345678900", StatusPedido.RECEBIDO, List.of(1L));
        dto.setCpf("12345678900");
        dto.setProdutosId(List.of(1L));

        PedidoEntity pedidoAlterado = new PedidoEntity();
        pedidoAlterado.setId(2L);

        when(pedidoService.alterarPedido(eq(2L), any())).thenReturn(Optional.of(pedidoAlterado));

        mockMvc.perform(put("/pedido/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L));
    }

    @Test
    void alterarPedido_deveRetornarNotFoundSeFalhar() throws Exception {
        PedidoDTO dto = new PedidoDTO("12345678900", StatusPedido.RECEBIDO, List.of(1L));

        when(pedidoService.alterarPedido(eq(99L), any())).thenReturn(Optional.empty());

        mockMvc.perform(put("/pedido/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void definirProximaOperacao_deveRetornarPedidoAtualizado() throws Exception {
        PedidoEntity pedido = new PedidoEntity();
        pedido.setId(1L);

        when(pedidoService.definirProximaOperacao(1L)).thenReturn(Optional.of(pedido));

        mockMvc.perform(put("/pedido/proxima-operacao/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void definirProximaOperacao_deveRetornarNotFoundSeNaoEncontrado() throws Exception {
        when(pedidoService.definirProximaOperacao(999L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/pedido/proxima-operacao/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
