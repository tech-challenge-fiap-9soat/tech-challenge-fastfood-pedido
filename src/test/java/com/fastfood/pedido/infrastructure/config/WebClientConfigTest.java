package com.fastfood.pedido.infrastructure.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {WebClientConfig.class, WebClientConfigTest.TestProps.class})
@TestPropertySource(properties = "external.api.url=http://localhost:8080/api")
class WebClientConfigTest {

    @Autowired
    private WebClient webClient;

    @Test
    void webClientDeveSerCriado() {
        assertNotNull(webClient);
    }

    @Configuration
    static class TestProps {
        @Bean
        public WebClient.Builder webClientBuilder() {
            return WebClient.builder();
        }
    }
}
