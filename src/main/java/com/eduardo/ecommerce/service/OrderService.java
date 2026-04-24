package com.eduardo.ecommerce.service;

import com.eduardo.ecommerce.dto.OrderCreateDTO;
import com.eduardo.ecommerce.dto.OrderItemRequestDTO;
import com.eduardo.ecommerce.dto.OrderResponseDTO;
import com.eduardo.ecommerce.exception.BusinessException;
import com.eduardo.ecommerce.exception.ResourceNotFoundException;
import com.eduardo.ecommerce.mapper.OrderMapper;
import com.eduardo.ecommerce.model.*;
import com.eduardo.ecommerce.repository.OrderRepository;
import com.eduardo.ecommerce.repository.ProductRepository;
import com.eduardo.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderMapper mapper;

    public OrderService(OrderRepository orderRepository,
                        UserRepository userRepository,
                        ProductRepository productRepository,
                        OrderMapper mapper) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.mapper = mapper;
    }

    @Transactional
    public OrderResponseDTO create(OrderCreateDTO dto) {
        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        Order order = new Order();
        order.setUser(user);

        List<OrderItem> items = new ArrayList<>();

        for (OrderItemRequestDTO itemDTO : dto.items()) {
            Product product = productRepository.findById(itemDTO.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado: " + itemDTO.productId()));

            if (product.getQuantity() < itemDTO.quantity()) {
                throw new BusinessException("Estoque insuficiente para o produto: " + product.getName());
            }

            product.setQuantity(product.getQuantity() - itemDTO.quantity());
            productRepository.save(product);

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemDTO.quantity());
            item.setPrice(product.getPrice());
            items.add(item);
        }

        order.setItems(items);
        return mapper.toResponseDTO(orderRepository.save(order));
    }

    public List<OrderResponseDTO> findAll() {
        return orderRepository.findAll()
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    public OrderResponseDTO findById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado"));
        return mapper.toResponseDTO(order);
    }

    public List<OrderResponseDTO> findByUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Usuário não encontrado");
        }
        return orderRepository.findByUserId(userId)
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public OrderResponseDTO updateStatus(Long id, OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado"));

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new BusinessException("Pedido cancelado não pode ser alterado");
        }

        order.setStatus(status);
        return mapper.toResponseDTO(orderRepository.save(order));
    }
}