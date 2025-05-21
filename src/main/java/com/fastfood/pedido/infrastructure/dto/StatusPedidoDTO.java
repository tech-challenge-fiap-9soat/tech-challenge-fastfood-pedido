package com.fastfood.pedido.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fastfood.pedido.infrastructure.enums.StatusPedido;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusPedidoDTO {
    private Long id;
    private String cpf;
    private StatusPedido statusPedido;
    private Double valorTotal;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime criadoEm;
}