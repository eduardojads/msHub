package com.github.eduardojads.ms_pagamento.dto;

import com.github.eduardojads.ms_pagamento.entity.Pagamento;
import com.github.eduardojads.ms_pagamento.entity.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PagamentoDTO {
    private Long id;
    @NotNull(message = "Campo obrigatório")
    @Positive(message = "O valor do pagamento deve ser um número positivo")
    private BigDecimal valor;
    @Size(max = 100, message = "Nome deve ter até 100 caracteres")
    private String nome;
    @Size(max = 19, message = "Número do cartão deve conter no máximo 19 caracteres")
    private String numeroDoCartao;
    @Size(min = 5, max = 5, message = "Validade do cartão deve conter 5 caracteres")
    private String validade; //MM/AA
    @Size(min = 3, max = 3, message = "O código de segurança deve conter 3 caracteres")
    private String codigoDeSeguranca;
    @Enumerated(value = EnumType.STRING)
    private Status status;
    @NotNull(message = "Pedido ID é obrigatório")
    private Long pedidoId;
    @NotNull(message = "Forma de pagamento ID é obrigatório")
    private Long formaDePagamentoId;


    public PagamentoDTO(Pagamento entity) {
        id = entity.getId();
        valor = entity.getValor();
        nome = entity.getNome();
        numeroDoCartao = entity.getNumeroDoCartao();
        validade = entity.getValidade();
        codigoDeSeguranca = entity.getCodigoDeSeguranca();
        status = entity.getStatus();
        pedidoId = entity.getPedidoId();
        formaDePagamentoId = entity.getFormaDePagamentoId();
    }
}
