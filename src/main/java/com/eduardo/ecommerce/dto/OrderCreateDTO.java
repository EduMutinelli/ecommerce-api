package com.eduardo.ecommerce.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderCreateDTO(

        @NotNull(message = "Usuário é obrigatório")
        Long userId,

        @NotEmpty(message = "Pedido deve ter pelo menos um item")
        List<OrderItemRequestDTO> items

) {}