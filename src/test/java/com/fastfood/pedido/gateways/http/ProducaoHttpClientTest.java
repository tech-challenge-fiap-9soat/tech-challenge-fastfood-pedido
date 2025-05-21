package com.fastfood.pedido.gateways.http;

import com.fastfood.pedido.infrastructure.dto.StatusPedidoDTO;
import com.fastfood.pedido.infrastructure.enums.StatusPedido;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatCode;

class ProducaoHttpClientTest {

    private static MockWebServer mockWebServer;

    private ProducaoHttpClient producaoHttpClient;

    @BeforeAll
    static void startServer() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void stopServer() throws IOException {
        mockWebServer.shutdown();
    }

    @BeforeEach
    void setUp() {
        WebClient webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();

        producaoHttpClient = new ProducaoHttpClient(webClient);

        // Simulando valores injetados via @Value
        setField(producaoHttpClient, "producaoBaseUrl", mockWebServer.url("/").toString());
        setField(producaoHttpClient, "pathPost", "pedido");
        setField(producaoHttpClient, "pathPatch", "pedido/{id}/status/{statusPedido}");
    }

    @Test
    void deveAtualizarStatusPedidoComSucesso() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200));

        assertThatCode(() -> producaoHttpClient.atualizacaoStatusPedido(123L, StatusPedido.EM_PREPARACAO))
                .doesNotThrowAnyException();
    }

    @Test
    void deveAdicionarStatusPedidoComSucesso() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200));

        StatusPedidoDTO dto = new StatusPedidoDTO(); // Adapte conforme seus campos
        assertThatCode(() -> producaoHttpClient.adicionarStatusPedido(dto))
                .doesNotThrowAnyException();
    }

    @Test
    void deveLancarErroQuandoPatchFalha() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(500));

        Assertions.assertThrows(RuntimeException.class, () ->
                producaoHttpClient.atualizacaoStatusPedido(456L, StatusPedido.FINALIZADO));
    }

    @Test
    void deveLancarErroQuandoPostFalha() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(500));

        StatusPedidoDTO dto = new StatusPedidoDTO();
        Assertions.assertThrows(RuntimeException.class, () ->
                producaoHttpClient.adicionarStatusPedido(dto));
    }

    private void setField(Object target, String fieldName, String value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
