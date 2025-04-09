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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    void setUp() throws Exception {

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

        //Simulando a criação de createPagamento
        Mockito.when(service.create(any())).thenReturn(dto);

        //Simulando o comportamento do service - updatePagamento
        //id existe
        Mockito.when(service.updatePagamento(eq(existingId), any())).thenReturn(dto);
        Mockito.when(service.updatePagamento(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);

        //Simlulando o comportamento do delete
        //id existe
        Mockito.doNothing().when(service).deletePagamento(existingId);
        //id não existe
        Mockito.doThrow(ResourceNotFoundException.class).when(service).deletePagamento(nonExistingId);

    }

    @Test
    public void getAllShouldReturnListPagamnetoDTO() throws Exception {

        //chamando a requisição com o método GET - endpoint /pagamentos
        ResultActions result = mockMvc.perform(get("/pagamentos")
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
    }

    @Test
    public void getByIdShouldReturnPagamentoDTOWhenIdExists() throws Exception {
        ResultActions result = mockMvc.perform(get("/pagamentos/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON));

        //Assertions
        result.andExpect(status().isOk());

        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.valor").exists());
        result.andExpect(jsonPath("$.status").exists());
    }

    @Test
    public void getByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() throws Exception {
        ResultActions result = mockMvc.perform(get("/pagamentos/{id}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void createPagamentoShouldReturnDtoCreated() throws Exception {
        PagamentoDTO newPagamentoDTO = Factory.createNewPagamentoDTO();

        String jsonRequestBody = objectMapper.writeValueAsString(newPagamentoDTO);

        mockMvc.perform(post("/pagamentos")
                        .content(jsonRequestBody)
                        .contentType(MediaType.APPLICATION_JSON) //Request
                        .accept(MediaType.APPLICATION_JSON))//Response
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.valor").exists())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.pedidoId").exists())
                .andExpect(jsonPath("$.formaDePagamentoId").exists());
    }

    @Test
    public void updatePagamentoShouldReturnPagamentoDTOWhenIdExists() throws Exception {
        //Convertendo JAVA para JSON
        String jsonRequestBody = objectMapper.writeValueAsString(dto);
        //PUT - tem corpo na requisição - JSON
        //é preciso passar o corpo da requisição
        mockMvc.perform(put("/pagamentos/{id}", existingId)
                        .content(jsonRequestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(existingId))
                .andExpect(jsonPath("$.valor").exists())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.pedidoId").exists())
                .andExpect(jsonPath("$.formaDePagamentoId").exists());


    }

    @Test
    public void updatePagamentoShoulReturnResourceNotFoundExceptionWhenIdDoesNotExists() throws Exception {
        String jsonRequestBody = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/pagamentos/{id}", nonExistingId)
                        .content(jsonRequestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void deletePagamentoShouldReturnPagamentoDTOWhenExists() throws Exception {

        mockMvc.perform(delete("/pagamentos/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void deletePagamentoShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() throws Exception {
        mockMvc.perform(delete("/pagamentos/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
