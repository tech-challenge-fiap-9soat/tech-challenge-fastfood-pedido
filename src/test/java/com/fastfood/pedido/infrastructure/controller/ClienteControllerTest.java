package com.fastfood.pedido.infrastructure.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fastfood.pedido.domain.entities.ClienteEntity;
import com.fastfood.pedido.infrastructure.dto.CadastroClienteDTO;
import com.fastfood.pedido.usecases.cliente.BuscaClientePorCpf;
import com.fastfood.pedido.usecases.cliente.CadastraCliente;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClienteController.class)
@Import(ClienteControllerTest.TestConfig.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BuscaClientePorCpf buscaClientePorCpf;

    @Autowired
    private CadastraCliente cadastraCliente;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public BuscaClientePorCpf buscaClientePorCpf() {
            return mock(BuscaClientePorCpf.class);
        }

        @Bean
        public CadastraCliente cadastraCliente() {
            return mock(CadastraCliente.class);
        }
    }

    @Test
    void identifica_deveRetornarClienteQuandoEncontrado() throws Exception {
        ClienteEntity cliente = new ClienteEntity();
        cliente.setCpf("12345678900");
        cliente.setNome("Maria");

        Mockito.when(buscaClientePorCpf.buscaPorCpf("12345678900"))
                .thenReturn(Optional.of(cliente));

        mockMvc.perform(get("/cliente/12345678900"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cpf").value("12345678900"))
                .andExpect(jsonPath("$.nome").value("Maria"));
    }

    @Test
    void identifica_deveRetornarNotFoundQuandoNaoEncontrado() throws Exception {
        Mockito.when(buscaClientePorCpf.buscaPorCpf("12345678900"))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/cliente/12345678900"))
                .andExpect(status().isNotFound());
    }

    @Test
    void cadastra_deveRetornarClienteCriadoQuandoSucesso() throws Exception {
        CadastroClienteDTO dto = new CadastroClienteDTO("joao@email.com", "João", "12345678900");

        ClienteEntity cliente = new ClienteEntity();
        cliente.setCpf(dto.getCpf());
        cliente.setNome(dto.getNome());
        cliente.setEmail(dto.getEmail());

        Mockito.when(cadastraCliente.cadastrarCliente(any()))
                .thenReturn(Optional.of(cliente));

        mockMvc.perform(post("/cliente/cadastro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cpf").value("12345678900"))
                .andExpect(jsonPath("$.nome").value("João"))
                .andExpect(jsonPath("$.email").value("joao@email.com"));
    }

    @Test
    void cadastra_deveRetornarNotFoundQuandoClienteNaoCriado() throws Exception {
        CadastroClienteDTO dto = new CadastroClienteDTO("email@invalido.com","Nome Inválido","00000000000");

        Mockito.when(cadastraCliente.cadastrarCliente(any()))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/cliente/cadastro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }
}
