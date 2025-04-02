package com.github.eduardojads.ms_pagamento.service;

import com.github.eduardojads.ms_pagamento.dto.PagamentoDTO;
import com.github.eduardojads.ms_pagamento.entity.Pagamento;
import com.github.eduardojads.ms_pagamento.repository.PagamentoRepository;
import com.github.eduardojads.ms_pagamento.service.exceptions.ResourceNotFoundException;
import com.github.eduardojads.ms_pagamento.tests.Factory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
public class PagamentoServiceTests {

    @InjectMocks
    private PagamentoService service;

    @Mock
    private PagamentoRepository repository;

    //Não usaremos DB
    //preparando os dados
    private Long existingId;
    private Long nonExistingId;

    //proximos testes
    private Pagamento pagamento;
    private PagamentoDTO dto;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 10L;

        //delete - quando o id existe
        Mockito.when(repository.existsById(existingId)).thenReturn(true);

        //delete - quando o id não existe
        Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);

        //não faça nada quando...
        Mockito.doNothing().when(repository).deleteById(existingId);

        //----------------------------------------
        //próximos testes
        pagamento = Factory.createPagamento();
        dto = new PagamentoDTO(pagamento);

        //getById (findById)
        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(pagamento));
        Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        //createPagamento (insert)
        Mockito.when(repository.save(any())).thenReturn(pagamento);

        //updatePagamento (update)
        Mockito.when(repository.getReferenceById(existingId)).thenReturn(pagamento);

        //updatePagamento - id não existe
        Mockito.when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("delete deveria não fazer nada quando Id existe")
    public void deleteShouldDoNothingWhenIdExists() {
        Assertions.assertDoesNotThrow(
                () -> {
                    service.deletePagamento(existingId);
                }
        );
    }

    @Test
    @DisplayName("delete deveria lançar exceção ResourceNotFoundException quando Id não existe")
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> {
                    service.deletePagamento(nonExistingId);
                }
        );

    }

    @Test
    @DisplayName("findById deveria retornar o id quando existir")
    public void findByIdShouldReturnPagamentoDTOWhenIdExists(){
        dto = service.getById(existingId);

        Assertions.assertNotNull(dto);
        Assertions.assertEquals(dto.getId(), existingId);
        Assertions.assertEquals(dto.getValor(), pagamento.getValor());
    }

    @Test
    public void getIdShouldReturnResourceNotFoundExceptionWhenIdDoesNotExists(){
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> {
                    service.getById(nonExistingId);
                }
        );
    }

    @Test
    public void createPagamentoShouldReturnPagamentoDTOWhenPagamentoIsCreated(){
        dto = service.create(dto);

        Assertions.assertNotNull(dto);
        Assertions.assertEquals(dto.getId(), pagamento.getId());
    }

    @Test
    public void updatePagamentoShouldReturnPagamentoDTOWhenIdExists(){
        dto = service.updatePagamento(pagamento.getId(), dto);

        Assertions.assertNotNull(dto);
        Assertions.assertEquals(dto.getId(), existingId);
        Assertions.assertEquals(dto.getValor(), pagamento.getValor());
    }

    @Test
    public void updatePagamentoShouldReturnResourceNotFoundExceptionWhenIdDoesNotExists(){

        Assertions.assertThrows(ResourceNotFoundException.class, () ->
        {
           service.updatePagamento(nonExistingId, dto);
        });
    }

}
