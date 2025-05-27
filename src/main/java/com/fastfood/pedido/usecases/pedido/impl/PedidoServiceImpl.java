package com.fastfood.pedido.usecases.pedido.impl;

import com.fastfood.pedido.domain.entities.ClienteEntity;
import com.fastfood.pedido.domain.entities.PedidoEntity;
import com.fastfood.pedido.domain.entities.ProdutoEntity;
import com.fastfood.pedido.domain.exception.BusinessException;
import com.fastfood.pedido.gateways.http.PagamentoHttpClient;
import com.fastfood.pedido.gateways.http.ProducaoHttpClient;
import com.fastfood.pedido.gateways.repository.ClienteGateway;
import com.fastfood.pedido.gateways.repository.PedidoGateway;
import com.fastfood.pedido.gateways.repository.ProdutoGateway;
import com.fastfood.pedido.infrastructure.dto.PedidoDTO;
import com.fastfood.pedido.infrastructure.dto.StatusPedidoDTO;
import com.fastfood.pedido.infrastructure.enums.ExceptionEnum;
import com.fastfood.pedido.infrastructure.enums.StatusPedido;
import com.fastfood.pedido.usecases.pedido.PedidoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoGateway pedidoGateway;

    @Autowired
    private ProdutoGateway produtoGateway;

    @Autowired
    private ClienteGateway clienteGateway;

    @Autowired
    private PagamentoHttpClient pagamentoHttpClient;

    @Autowired
    private ProducaoHttpClient producaoHttpClient;

    @Override
    public List<PedidoEntity> listarTodos() {
        return this.pedidoGateway.findAllToDisplay();
    }

    @Override
    public Optional<PedidoEntity> checkoutPedido(PedidoDTO pedidoDTO) {
        if (pedidoDTO.getProdutosId().isEmpty()) throw new BusinessException(ExceptionEnum.PEDIDO_INVALIDO);
        PedidoEntity pedidoEntity = pedidoGateway.save(this.montaPedido(pedidoDTO));
        producaoHttpClient.adicionarStatusPedido(this.montaStatusPedidoDTO(pedidoEntity));
        return Optional.of(pedidoEntity);
    }

    private StatusPedidoDTO montaStatusPedidoDTO(PedidoEntity pedidoEntity) {
        StatusPedidoDTO statusPedidoDTO = new StatusPedidoDTO();
        statusPedidoDTO.setId(pedidoEntity.getId());
        if(pedidoEntity.getCliente() != null) statusPedidoDTO.setCpf(pedidoEntity.getCliente().getCpf());
        statusPedidoDTO.setStatusPedido(pedidoEntity.getStatusPedido());
        statusPedidoDTO.setValorTotal(pedidoEntity.getValorTotal());

        return statusPedidoDTO;
    }

    private PedidoEntity montaPedido(PedidoDTO pedidoDTO) {
        PedidoEntity pedido = new PedidoEntity();

        Optional<ClienteEntity> cliente = clienteGateway.findByCpf(pedidoDTO.getCpf());
        cliente.ifPresent(clienteEntity -> pedido.setCliente(clienteEntity));

        List<ProdutoEntity> produtos = produtoGateway.findAllByIdIn(pedidoDTO.getProdutosId());
        pedido.setProdutos(produtos);

        pedido.setStatusPedido(StatusPedido.RECEBIDO);
        pedido.setValorTotal(produtos.stream().map(ProdutoEntity::getPreco).reduce(0.0, Double::sum));

        return pedido;
    }

    @Override
    public Optional<PedidoEntity> alterarPedido(Long id, PedidoDTO pedidoDTO) {
        PedidoEntity pedidoEntity = this.pedidoGateway.findById(id).orElseThrow(IllegalArgumentException::new);

        List<ProdutoEntity> produtos = produtoGateway.findAllByIdIn(pedidoDTO.getProdutosId());
        pedidoEntity.setProdutos(produtos);

        pedidoEntity.setValorTotal(produtos.stream().map(ProdutoEntity::getPreco).reduce(0.0, Double::sum));

        producaoHttpClient.atualizacaoStatusPedido(pedidoEntity.getId(), pedidoEntity.getStatusPedido());

        return Optional.of(pedidoGateway.save(pedidoEntity));
    }

    @Override
    public Optional<PedidoEntity> definirProximaOperacao(Long id) {
        PedidoEntity pedidoEntity = this.pedidoGateway.findById(id).orElseThrow(IllegalArgumentException::new);
        StatusPedido proximaOperacao = pedidoEntity.getStatusPedido().getNext();

        String pagamentoAprovado = pagamentoHttpClient.consultaPagamentoAprovado(pedidoEntity.getId());

        if (StatusPedido.EM_PREPARACAO.equals(proximaOperacao) && pagamentoAprovado.equalsIgnoreCase("Reprovado")) {
            throw new BusinessException(ExceptionEnum.PAGAMENTO_PENDENTE, pedidoEntity.getId().toString());
        }

        pedidoEntity.setStatusPedido(proximaOperacao);

        producaoHttpClient.atualizacaoStatusPedido(pedidoEntity.getId(), pedidoEntity.getStatusPedido());

        return Optional.of(pedidoGateway.save(pedidoEntity));
    }
}
