package com.fastfood.pedido.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    public WebClient webClient;

    @Bean
    public WebClient webClient(WebClient.Builder webClientBuilder, @Value("${external.api.url}") String externalApiUrl) {
        this.webClient = webClientBuilder.baseUrl(externalApiUrl).build();
        return webClient;
    }
}
