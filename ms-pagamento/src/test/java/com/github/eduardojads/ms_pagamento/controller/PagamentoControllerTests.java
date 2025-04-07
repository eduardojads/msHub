package com.github.eduardojads.ms_pagamento.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.eduardojads.ms_pagamento.dto.PagamentoDTO;
import com.github.eduardojads.ms_pagamento.service.PagamentoService;
import com.github.eduardojads.ms_pagamento.service.exceptions.ResourceNotFoundException;
import com.github.eduardojads.ms_pagamento.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.lang.module.ResolutionException;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class PagamentoControllerTests {


    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PagamentoService service;

    private PagamentoDTO dto;
    private Long existingId;
    private Long nonExistingId;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws Exception{

        existingId = 1L;
        nonExistingId = 100L;


        dto = Factory.createPagamentoDTO();

        //Listando o pagamento
        List<PagamentoDTO> list = List.of(dto);

        //Simulando o comportamento do getAll com Mockito
        Mockito.when(service.getAll()).thenReturn(list);

        //simulando o comportamento do getId - Service
        //id existe
        Mockito.when(service.getById(existingId)).thenReturn(dto);

        //id não existe - lança exception
        Mockito.when(service.getById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        
    }

    @Test
    public void getAllShouldReturnListPagamnetoDTO() throws Exception{

        //chamando a requisição com o método GET - endpoint /pagamentos
        ResultActions result = mockMvc.perform(get("/pagamentos")
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
    }

    @Test
    public void getByIdShouldReturnPagamentoDTOWhenIdExists() throws Exception{
        ResultActions result = mockMvc.perform(get("/pagamentos/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON));

        //Assertions
        result.andExpect(status().isOk());

        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.valor").exists());
        result.andExpect(jsonPath("$.status").exists());
    }

    @Test
    public void getByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() throws Exception{
        ResultActions result = mockMvc.perform(get("/pagamentos/{id}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }
}
