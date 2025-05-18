package com.fastfood.pedido.infrastructure.controller;

import com.fastfood.pedido.domain.entities.ProdutoEntity;
import com.fastfood.pedido.infrastructure.dto.ProdutoDTO;
import com.fastfood.pedido.infrastructure.enums.CategoriaProduto;
import com.fastfood.pedido.usecases.produto.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/produto")
public class ProdutoController {
    @Autowired
    private ProdutoService produtoService;

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<ProdutoEntity>> listaProdutosCategoria(@PathVariable CategoriaProduto categoria) {
        return ResponseEntity.ok(produtoService.listarTodosDaCategoria(categoria));
    }

    @PostMapping
    public ResponseEntity<ProdutoEntity> cadastrarProduto(@RequestBody ProdutoDTO produtoDTO) {
        Optional<ProdutoEntity> produto = produtoService.cadastrarProduto(produtoDTO);
        return produto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoEntity> editarProduto(@PathVariable Long id, @RequestBody ProdutoDTO produtoDTO) {
        Optional<ProdutoEntity> produto = produtoService.editarProduto(id, produtoDTO);
        return produto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProdutoEntity> deletarProduto(@PathVariable Long id) {
        this.produtoService.deletarProduto(id);
        return ResponseEntity.ok().build();
    }
}
