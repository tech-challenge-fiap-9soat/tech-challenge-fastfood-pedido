package com.fastfood.pedido.gateways.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@Slf4j
public class PagamentoHttpClient {

    @Value("${external.api.pagamento-base-url}") //TODO definir endpoint
    private String pagamentoBaseUrl;

    private final WebClient webClient;

    public PagamentoHttpClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public boolean consultaPagamentoAprovado(Long pedidoId) {
        try {
            return Boolean.TRUE.equals(webClient.get().uri(pagamentoBaseUrl) //TODO montar uri com pedidoId
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .doOnSuccess(response -> log.info("Resposta da aplicação de pagamento: {}", response))
                    .doOnError(error -> log.error("Erro ao consultar pagamento: {}", error.getMessage()))
                    .block());
        } catch (Exception e) {
            log.error("Erro na comunicação com a aplicação externa: {}", e.getMessage());
            throw e;
        }
    }
}
