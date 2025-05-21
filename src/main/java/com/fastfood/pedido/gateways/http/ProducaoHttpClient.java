package com.fastfood.pedido.gateways.http;

import com.fastfood.pedido.infrastructure.dto.StatusPedidoDTO;
import com.fastfood.pedido.infrastructure.enums.StatusPedido;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@Slf4j
public class ProducaoHttpClient {
    @Value("${external.api.producao.url}")
    private String producaoBaseUrl;

    @Value("${external.api.producao.path.post}")
    private String pathPost;

    @Value("${external.api.producao.path.patch}")
    private String pathPatch;

    private final WebClient webClient;

    public ProducaoHttpClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public void atualizacaoStatusPedido(Long pedidoId, StatusPedido statusPedido) {
        try {
            webClient.patch().uri(
                    uriBuilder -> uriBuilder
                            .path("/pedido/{id}/status/{statusPedido}")
                            .build(pedidoId, statusPedido.name()))
                    .retrieve()
                    .toBodilessEntity()
                    .doOnSuccess(response -> log.info("Resposta da aplicação fila de producao: {}", response))
                    .doOnError(error -> log.error("Erro ao consultar fila de producao: {}", error.getMessage()))
                    .block();
        } catch (Exception e) {
            log.error("Erro na comunicação com a aplicação externa: {}", e.getMessage());
            throw e;
        }
    }

    public void adicionarStatusPedido(StatusPedidoDTO statusPedidoDTO) {
        try {
            webClient.post().uri(producaoBaseUrl + pathPost)
                    .bodyValue(statusPedidoDTO)
                    .retrieve()
                    .toBodilessEntity()
                    .doOnSuccess(response -> log.info("Resposta da aplicação fila de producao: {}", response))
                    .doOnError(error -> log.error("Erro ao consultar fila de producao: {}", error.getMessage()))
                    .block();
        } catch (Exception e) {
            log.error("Erro na comunicação com a aplicação externa: {}", e.getMessage());
            throw e;
        }
    }


}
