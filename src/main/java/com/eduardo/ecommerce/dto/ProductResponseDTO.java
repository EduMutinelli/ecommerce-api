package com.eduardo.ecommerce.dto;

public record ProductResponseDTO(
        Long id,
        String name,
        Double price,
        Integer quantity
) {}