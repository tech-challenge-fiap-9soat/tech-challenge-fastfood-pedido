package com.fastfood.pedido.infrastructure.repository;

import com.fastfood.pedido.domain.entities.ProdutoEntity;
import com.fastfood.pedido.infrastructure.enums.CategoriaProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaProdutoRepository extends JpaRepository<ProdutoEntity, Long> {

    List<ProdutoEntity> findAllByCategoria(CategoriaProduto categoria);

    List<ProdutoEntity> findAllByIdIn(List<Long> ids);

}
