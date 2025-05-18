package com.fastfood.pedido.gateways.repository;


import com.fastfood.pedido.domain.entities.ProdutoEntity;
import com.fastfood.pedido.infrastructure.enums.CategoriaProduto;

import java.util.List;

public interface ProdutoGateway {

    List<ProdutoEntity> findAllByCategoria(CategoriaProduto categoriaProduto);

    List<ProdutoEntity> findAllByIdIn(List<Long> ids);

    ProdutoEntity save(ProdutoEntity produtoEntity);

    void deleteById(Long id);

}
