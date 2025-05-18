package com.fastfood.pedido.usecases.produto;

import com.fastfood.pedido.domain.entities.ProdutoEntity;
import com.fastfood.pedido.infrastructure.dto.ProdutoDTO;
import com.fastfood.pedido.infrastructure.enums.CategoriaProduto;

import java.util.List;
import java.util.Optional;

public interface ProdutoService {
    List<ProdutoEntity> listarTodosDaCategoria(CategoriaProduto categoriaProduto);

    Optional<ProdutoEntity> cadastrarProduto(ProdutoDTO produtoDTO);

    Optional<ProdutoEntity> editarProduto(Long id, ProdutoDTO produtoDTO);

    void deletarProduto(Long id);
}
