package com.fastfood.pedido.gateways.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
@Slf4j
public class PagamentoHttpClient {

    @Value("${external.api.pagamento.url}")
    private String pagamentoBaseUrl;

    @Value("${external.api.pagamento.path.get}")
    private String pathStatusPagamento;

    private final WebClient webClient;

    public PagamentoHttpClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public String consultaPagamentoAprovado(Long pedidoId) {
        try {
            URI uri = UriComponentsBuilder
                    .fromHttpUrl(pagamentoBaseUrl)
                    .path(pathStatusPagamento)
                    .build(pedidoId);
            return webClient.get().uri(uri)
                    .retrieve()
                    .bodyToMono(String.class)
                    .doOnSuccess(response -> log.info("Resposta da aplicação de pagamento: {}", response))
                    .doOnError(error -> log.error("Erro ao consultar pagamento: {}", error.getMessage()))
                    .block();
        } catch (Exception e) {
            log.error("Erro na comunicação com a aplicação externa: {}", e.getMessage());
            throw e;
        }
    }
}
