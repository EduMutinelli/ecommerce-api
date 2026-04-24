package com.eduardo.ecommerce.mapper;

import com.eduardo.ecommerce.dto.OrderItemResponseDTO;
import com.eduardo.ecommerce.dto.OrderResponseDTO;
import com.eduardo.ecommerce.model.Order;
import com.eduardo.ecommerce.model.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {

    public OrderResponseDTO toResponseDTO(Order order) {
        List<OrderItemResponseDTO> items = order.getItems()
                .stream()
                .map(this::toItemResponseDTO)
                .toList();

        Double total = items.stream()
                .mapToDouble(item -> item.price() * item.quantity())
                .sum();

        return new OrderResponseDTO(
                order.getId(),
                order.getUser().getId(),
                order.getUser().getName(),
                order.getStatus(),
                order.getCreatedAt(),
                items,
                total
        );
    }

    private OrderItemResponseDTO toItemResponseDTO(OrderItem item) {
        return new OrderItemResponseDTO(
                item.getId(),
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getQuantity(),
                item.getPrice()
        );
    }
}