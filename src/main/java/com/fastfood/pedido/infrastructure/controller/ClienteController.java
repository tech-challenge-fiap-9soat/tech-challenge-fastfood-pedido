package com.fastfood.pedido.infrastructure.controller;

import com.fastfood.pedido.domain.entities.ClienteEntity;
import com.fastfood.pedido.infrastructure.dto.CadastroClienteDTO;
import com.fastfood.pedido.usecases.cliente.BuscaClientePorCpf;
import com.fastfood.pedido.usecases.cliente.CadastraCliente;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/cliente")
@AllArgsConstructor
public class ClienteController {

    private final BuscaClientePorCpf buscaClientePorCpf;
    private final CadastraCliente cadastraCliente;

    @GetMapping("/{cpf}")
    public ResponseEntity<ClienteEntity> identifica(@PathVariable String cpf) {
        Optional<ClienteEntity> cliente = buscaClientePorCpf.buscaPorCpf(cpf);
        return cliente.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/cadastro")
    public ResponseEntity<ClienteEntity> cadastra(@RequestBody CadastroClienteDTO cadastro) {
        Optional<ClienteEntity> cliente = cadastraCliente.cadastrarCliente(cadastro);
        return cliente.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
