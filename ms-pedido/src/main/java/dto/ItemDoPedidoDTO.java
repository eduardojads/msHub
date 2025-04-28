package dto;

import entities.ItemDoPedido;
import jakarta.validation.constraints.NotBlank;
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
public class ItemDoPedidoDTO {

    private Long id;

    @NotNull(message = "Quantidade requerida")
    @Positive(message = "A quantidade deve ser um valor positivo")
    private Integer quantidade;

    @NotBlank(message = "Descrição requerida")
    private String descricao;

    @NotNull(message = "Valor unitário requerido")
    @Positive(message = "O valor unitário deve ser um valor positivo")
    private BigDecimal valorUnitario;

    public ItemDoPedidoDTO(ItemDoPedido entity) {
         id = entity.getId();
         quantidade = entity.getQuantidade();
         descricao = entity.getDescricao();
         valorUnitario = entity.getValorUnitario();
    }
}


