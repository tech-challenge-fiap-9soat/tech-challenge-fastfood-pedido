package com.fastfood.pedido.gateways.repository.impl;

import com.fastfood.pedido.domain.entities.ProdutoEntity;
import com.fastfood.pedido.gateways.repository.ProdutoGateway;
import com.fastfood.pedido.infrastructure.enums.CategoriaProduto;
import com.fastfood.pedido.infrastructure.repository.JpaProdutoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ProdutoRepositoryGateway implements ProdutoGateway {

    private final JpaProdutoRepository produtoRepository;

    @Override
    public List<ProdutoEntity> findAllByCategoria(CategoriaProduto categoriaProduto) {
        return produtoRepository.findAllByCategoria(categoriaProduto);
    }

    @Override
    public List<ProdutoEntity> findAllByIdIn(List<Long> ids) {
        return produtoRepository.findAllByIdIn(ids);
    }

    @Override
    public ProdutoEntity save(ProdutoEntity produtoEntity) {
        return produtoRepository.save(produtoEntity);
    }

    @Override
    public void deleteById(Long id) {
        produtoRepository.deleteById(id);
    }
}
