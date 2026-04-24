package com.eduardo.ecommerce.dto;

import com.eduardo.ecommerce.model.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDTO(
        Long id,
        Long userId,
        String userName,
        OrderStatus status,
        LocalDateTime createdAt,
        List<OrderItemResponseDTO> items,
        Double total
) {}