Feature:
  Scenario: Cadastrar Pedido
    When Recebo um novo pedido
    Then o pedido é salvo com sucesso
    And o pedido é retornado