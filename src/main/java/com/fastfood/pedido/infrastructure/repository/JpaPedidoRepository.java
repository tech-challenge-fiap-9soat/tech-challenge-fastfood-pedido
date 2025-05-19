package com.fastfood.pedido.infrastructure.repository;

import com.fastfood.pedido.domain.entities.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaPedidoRepository extends JpaRepository<PedidoEntity, Long> {

    @Query("from PedidoEntity p where p.statusPedido != 'FINALIZADO' order by p.statusPedido desc, p.criadoEm asc")
    List<PedidoEntity> findAllToDisplay();

}
