package com.eduardo.ecommerce.dto;

import jakarta.validation.constraints.*;

public record ProductCreateDTO(

        @NotBlank(message = "Nome é obrigatório")
        String name,

        @NotNull(message = "Preço é obrigatório")
        @Positive(message = "Preço deve ser positivo")
        Double price,

        @NotNull(message = "Quantidade é obrigatória")
        @Min(value = 0, message = "Quantidade não pode ser negativa")
        Integer quantity

) {}