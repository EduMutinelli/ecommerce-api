package com.eduardo.ecommerce.service;

import com.eduardo.ecommerce.dto.OrderCreateDTO;
import com.eduardo.ecommerce.dto.OrderItemRequestDTO;
import com.eduardo.ecommerce.dto.OrderItemResponseDTO;
import com.eduardo.ecommerce.dto.OrderResponseDTO;
import com.eduardo.ecommerce.exception.BusinessException;
import com.eduardo.ecommerce.exception.ResourceNotFoundException;
import com.eduardo.ecommerce.mapper.OrderMapper;
import com.eduardo.ecommerce.model.*;
import com.eduardo.ecommerce.repository.OrderRepository;
import com.eduardo.ecommerce.repository.ProductRepository;
import com.eduardo.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderMapper mapper;

    @InjectMocks
    private OrderService service;

    private User user;
    private Product product;
    private Order order;
    private OrderResponseDTO responseDTO;
    private OrderCreateDTO createDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setName("Eduardo");
        user.setEmail("edu@email.com");
        user.setRole(Role.ROLE_USER);

        product = new Product();
        product.setName("Teclado Mecânico");
        product.setPrice(299.90);
        product.setQuantity(10);

        order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);

        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(2);
        item.setPrice(299.90);
        item.setOrder(order);
        order.setItems(List.of(item));

        createDTO = new OrderCreateDTO(
                1L,
                List.of(new OrderItemRequestDTO(1L, 2))
        );

        responseDTO = new OrderResponseDTO(
                1L,
                1L,
                "Eduardo",
                OrderStatus.PENDING,
                LocalDateTime.now(),
                List.of(new OrderItemResponseDTO(1L, 1L, "Teclado Mecânico", 2, 299.90)),
                599.80
        );
    }

    @Test
    @DisplayName("Deve criar pedido com sucesso")
    void deveCriarPedidoComSucesso() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(mapper.toResponseDTO(order)).thenReturn(responseDTO);

        // Act
        OrderResponseDTO resultado = service.create(createDTO);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.status()).isEqualTo(OrderStatus.PENDING);
        assertThat(resultado.total()).isEqualTo(599.80);
        verify(orderRepository).save(any(Order.class));
        verify(productRepository).save(any(Product.class));
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao criar pedido com usuário inexistente")
    void deveLancarExcecaoQuandoUsuarioInexistenteNaCriacao() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> service.create(createDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Usuário não encontrado");

        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao criar pedido com produto inexistente")
    void deveLancarExcecaoQuandoProdutoInexistenteNaCriacao() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> service.create(createDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Produto não encontrado");

        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar BusinessException quando estoque insuficiente")
    void deveLancarExcecaoQuandoEstoqueInsuficiente() {
        // Arrange
        product.setQuantity(1); // estoque menor que a quantidade pedida (2)
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // Act & Assert
        assertThatThrownBy(() -> service.create(createDTO))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Estoque insuficiente");

        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve retornar lista de pedidos")
    void deveRetornarListaDePedidos() {
        // Arrange
        when(orderRepository.findAll()).thenReturn(List.of(order));
        when(mapper.toResponseDTO(order)).thenReturn(responseDTO);

        // Act
        List<OrderResponseDTO> resultado = service.findAll();

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).status()).isEqualTo(OrderStatus.PENDING);
    }

    @Test
    @DisplayName("Deve retornar pedido por id com sucesso")
    void deveRetornarPedidoPorId() {
        // Arrange
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(mapper.toResponseDTO(order)).thenReturn(responseDTO);

        // Act
        OrderResponseDTO resultado = service.findById(1L);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao buscar pedido inexistente")
    void deveLancarExcecaoQuandoPedidoNaoEncontrado() {
        // Arrange
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Pedido não encontrado");
    }

    @Test
    @DisplayName("Deve retornar pedidos por usuário com sucesso")
    void deveRetornarPedidosPorUsuario() {
        // Arrange
        when(userRepository.existsById(1L)).thenReturn(true);
        when(orderRepository.findByUserId(1L)).thenReturn(List.of(order));
        when(mapper.toResponseDTO(order)).thenReturn(responseDTO);

        // Act
        List<OrderResponseDTO> resultado = service.findByUser(1L);

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).userName()).isEqualTo("Eduardo");
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao buscar pedidos de usuário inexistente")
    void deveLancarExcecaoQuandoUsuarioInexistenteNoBuscarPorUsuario() {
        // Arrange
        when(userRepository.existsById(99L)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> service.findByUser(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Usuário não encontrado");
    }

    @Test
    @DisplayName("Deve atualizar status do pedido com sucesso")
    void deveAtualizarStatusComSucesso() {
        // Arrange
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(mapper.toResponseDTO(order)).thenReturn(
                new OrderResponseDTO(1L, 1L, "Eduardo", OrderStatus.CONFIRMED,
                        LocalDateTime.now(), responseDTO.items(), 599.80)
        );

        // Act
        OrderResponseDTO resultado = service.updateStatus(1L, OrderStatus.CONFIRMED);

        // Assert
        assertThat(resultado.status()).isEqualTo(OrderStatus.CONFIRMED);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException ao atualizar status de pedido cancelado")
    void deveLancarExcecaoQuandoPedidoJaCancelado() {
        // Arrange
        order.setStatus(OrderStatus.CANCELLED);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        // Act & Assert
        assertThatThrownBy(() -> service.updateStatus(1L, OrderStatus.CONFIRMED))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Pedido cancelado não pode ser alterado");

        verify(orderRepository, never()).save(any());
    }
}