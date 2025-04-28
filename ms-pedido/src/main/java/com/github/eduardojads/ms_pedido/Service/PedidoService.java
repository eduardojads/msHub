package com.github.eduardojads.ms_pedido.Service;

import com.github.eduardojads.ms_pedido.dto.PedidoDTO;
import com.github.eduardojads.ms_pedido.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository repository;

    @Transactional(readOnly = true)
    public List<PedidoDTO> findAllPedidos(){

        return repository.findAll().stream().map(PedidoDTO::new).toList();
    }

}
