package com.github.eduardojads.ms_pedido.service.exceptions;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String message) {
        super (message);
    }
}
