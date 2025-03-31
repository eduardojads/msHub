package com.github.eduardojads.ms_pagamento.tests;

import com.github.eduardojads.ms_pagamento.entity.Pagamento;
import com.github.eduardojads.ms_pagamento.entity.Status;

import java.math.BigDecimal;

public class Factory {

    public static Pagamento createPagamento(){
        Pagamento pagamento = new Pagamento(1L, BigDecimal.valueOf(32.25),
                "Jon Snow", "2364325423543621",
                "07/32", "585", Status.CRIADO, 1L, 2L);
        return pagamento;
    }

}
