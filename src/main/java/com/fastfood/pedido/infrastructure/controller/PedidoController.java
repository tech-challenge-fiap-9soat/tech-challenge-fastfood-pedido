package com.fastfood.pedido.infrastructure.controller;

import com.fastfood.pedido.domain.entities.PedidoEntity;
import com.fastfood.pedido.infrastructure.dto.PedidoDTO;
import com.fastfood.pedido.usecases.pedido.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pedido")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    public ResponseEntity<List<PedidoEntity>> listaPedidos() {
        return ResponseEntity.ok(pedidoService.listarTodos());
    }

    @PostMapping
    public ResponseEntity<PedidoEntity> cadastrarPedido(@RequestBody PedidoDTO pedidoDTO) {
        Optional<PedidoEntity> produto = pedidoService.checkoutPedido(pedidoDTO);
        return produto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoEntity> alterarStatusPedido(@PathVariable Long id, @RequestBody PedidoDTO pedidoDTO) {
        Optional<PedidoEntity> produto = pedidoService.alterarPedido(id, pedidoDTO);
        return produto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/proxima-operacao/{id}")
    public ResponseEntity<PedidoEntity> definirProximaOperacao(@PathVariable Long id) {
        Optional<PedidoEntity> produto = pedidoService.definirProximaOperacao(id);
        return produto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
