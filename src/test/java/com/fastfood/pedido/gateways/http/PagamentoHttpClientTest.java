package com.fastfood.pedido.gateways.http;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.*;

class PagamentoHttpClientIntegrationTest {

    private MockWebServer mockWebServer;
    private PagamentoHttpClient pagamentoHttpClient;

    @BeforeEach
    void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        String baseUrl = mockWebServer.url("/pagamento/status").toString();

        WebClient webClient = WebClient.builder().baseUrl(baseUrl).build();
        pagamentoHttpClient = new PagamentoHttpClient(webClient);

        setPrivateField(pagamentoHttpClient, "pagamentoBaseUrl", mockWebServer.url("/").toString());
        setPrivateField(pagamentoHttpClient, "pathStatusPagamento", "/fastfood/pagamento/{idPedido}");
    }

    @AfterEach
    void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    void deveRetornarAprovadoQuandoRespostaEhAprovado() {
        mockWebServer.enqueue(new MockResponse()
                .setBody("Aprovado")
                .addHeader("Content-Type", "application/json"));

        String resultado = pagamentoHttpClient.consultaPagamentoAprovado(1L);
        assertEquals("Aprovado", resultado);
    }

    @Test
    void deveRetornarReprovadoQuandoRespostaEhReprovado() {
        mockWebServer.enqueue(new MockResponse()
                .setBody("Reprovado")
                .addHeader("Content-Type", "application/json"));

        String resultado = pagamentoHttpClient.consultaPagamentoAprovado(2L);
        assertEquals("Reprovado", resultado);
    }

    @Test
    void deveLancarExcecaoQuandoErroHttp() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(500));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pagamentoHttpClient.consultaPagamentoAprovado(3L);
        });

        assertTrue(exception.getMessage().contains("500"));
    }

    private void setPrivateField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
