package com.eduardo.ecommerce.dto;

public record OrderItemResponseDTO(
        Long id,
        Long productId,
        String productName,
        Integer quantity,
        Double price
) {}