package com.github.eduardojads.ms_pedido.controller;

import com.github.eduardojads.ms_pedido.Service.PedidoService;
import com.github.eduardojads.ms_pedido.dto.PedidoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService service;

    @GetMapping
    public ResponseEntity<List<PedidoDTO>> getAllPedidos(){

        List<PedidoDTO> list = service.findAllPedidos();
        return ResponseEntity.ok(list);
    }
}
