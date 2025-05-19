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

        setPrivateField(pagamentoHttpClient, "pagamentoBaseUrl", "");
    }

    @AfterEach
    void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    void deveRetornarTrueQuandoRespostaEhTrue() {
        mockWebServer.enqueue(new MockResponse()
                .setBody("true")
                .addHeader("Content-Type", "application/json"));

        boolean resultado = pagamentoHttpClient.consultaPagamentoAprovado(1L);
        assertTrue(resultado);
    }

    @Test
    void deveRetornarFalseQuandoRespostaEhFalse() {
        mockWebServer.enqueue(new MockResponse()
                .setBody("false")
                .addHeader("Content-Type", "application/json"));

        boolean resultado = pagamentoHttpClient.consultaPagamentoAprovado(2L);
        assertFalse(resultado);
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
