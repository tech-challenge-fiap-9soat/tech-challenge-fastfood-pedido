package com.fastfood.pedido.infrastructure.repository;

import com.fastfood.pedido.domain.entities.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaClienteRepository extends JpaRepository<ClienteEntity, Long> {

    Optional<ClienteEntity> findByCpf(String cpf);

}