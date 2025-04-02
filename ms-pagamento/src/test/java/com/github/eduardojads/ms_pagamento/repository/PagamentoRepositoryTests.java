package com.github.eduardojads.ms_pagamento.repository;


import com.github.eduardojads.ms_pagamento.entity.Pagamento;
import com.github.eduardojads.ms_pagamento.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class PagamentoRepositoryTests {

    @Autowired
    private PagamentoRepository repository;

    //Atributos
    private Long existingId;
    private Long nonExistingId;
    private Long countTotalPagamento;

    @BeforeEach
    void setUp() throws Exception {
        //Arrange
        existingId = 1L;
        nonExistingId = 100L;
        countTotalPagamento = 3L;
    }

    @Test
    public void deleteShouldDeleteObcjectiveWhenIdExists() {

        Long existsId = 1L;

        repository.deleteById(existsId);

        Optional<Pagamento> result = repository.findById(existsId);
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Dado parâmetros válidos e Id nulo" +
            "quando chamar Criar Pagamento" +
            "então deve instanciar um pagamento")
    public void givenValidParamsAndIdIsNull_whenCallCreatePagamento_thenInstantiateAPagamento() {
        Pagamento pagamento = Factory.createPagamento();
        pagamento.setId(null);
        pagamento = repository.save(pagamento);
        Assertions.assertNotNull(pagamento.getId());

        //Verifica se o id gerado é o próximo
        Assertions.assertEquals(countTotalPagamento + 1, pagamento.getId());
    }

    @Test
    @DisplayName("dado um ID não existente quando chamar findById então deve retornar um Optional vazio")
    public void givenANonExistingId_whenCallFindById_thenReturnAnEmptyOptional(){

        Optional<Pagamento> result = repository.findById(nonExistingId);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("dado um ID não existente quando chamar findById então deve retornar um Optional não vazio")
    public void givenANonExistingId_whenCallFindById_thenReturnNonEmptyOptional(){

        Optional<Pagamento> result = repository.findById(existingId);
        Assertions.assertTrue(result.isPresent());
    }

}
