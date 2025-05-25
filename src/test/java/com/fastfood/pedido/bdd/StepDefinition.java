package com.fastfood.pedido.bdd;

import com.fastfood.pedido.infrastructure.dto.PedidoDTO;
import com.fastfood.pedido.infrastructure.enums.StatusPedido;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class StepDefinition {

    private Response response;

    private final String ENDPOINT_API_PEDIDO = "http://localhost:8080/fastfood/pedido";

    @When("Recebo um novo pedido")
    public void recebo_um_novo_pedido() {
        PedidoDTO novoPedido = new PedidoDTO("41389872076", List.of(1L,2L,3L));
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(novoPedido)
                .when()
                .post(ENDPOINT_API_PEDIDO);
    }

    @Then("o pedido é salvo com sucesso")
    public void o_pedido_é_salvo_com_sucesso() {
        response.then().statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/usuario-schema.json"));
    }

    @Then("o pedido é retornado")
    public void o_pedido_é_retornado() {
        response.then()
                .body(matchesJsonSchemaInClasspath("schemas/usuario-schema.json"));
    }
}
