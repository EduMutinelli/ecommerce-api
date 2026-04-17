package com.eduardo.ecommerce.service;

import com.eduardo.ecommerce.dto.ProductCreateDTO;
import com.eduardo.ecommerce.dto.ProductResponseDTO;
import com.eduardo.ecommerce.dto.ProductUpdateDTO;
import com.eduardo.ecommerce.exception.ResourceNotFoundException;
import com.eduardo.ecommerce.mapper.ProductMapper;
import com.eduardo.ecommerce.model.Product;
import com.eduardo.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @Mock
    private ProductMapper mapper;

    @InjectMocks
    private ProductService service;

    private Product product;
    private ProductResponseDTO responseDTO;
    private ProductCreateDTO createDTO;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setName("Teclado Mecânico");
        product.setPrice(299.90);
        product.setQuantity(50);

        createDTO = new ProductCreateDTO("Teclado Mecânico", 299.90, 50);

        responseDTO = new ProductResponseDTO(1L, "Teclado Mecânico", 299.90, 50);
    }

    @Test
    @DisplayName("Deve criar produto com sucesso")
    void deveCriarProdutoComSucesso() {
        // Arrange
        when(mapper.toEntity(createDTO)).thenReturn(product);
        when(repository.save(any(Product.class))).thenReturn(product);
        when(mapper.toResponseDTO(product)).thenReturn(responseDTO);

        // Act
        ProductResponseDTO resultado = service.create(createDTO);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.name()).isEqualTo("Teclado Mecânico");
        assertThat(resultado.price()).isEqualTo(299.90);
        verify(repository).save(any(Product.class));
    }

    @Test
    @DisplayName("Deve retornar lista de produtos")
    void deveRetornarListaDeProdutos() {
        // Arrange
        when(repository.findAll()).thenReturn(List.of(product));
        when(mapper.toResponseDTO(product)).thenReturn(responseDTO);

        // Act
        List<ProductResponseDTO> resultado = service.findAll();

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).name()).isEqualTo("Teclado Mecânico");
    }

    @Test
    @DisplayName("Deve retornar produto por id com sucesso")
    void deveRetornarProdutoPorId() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(product));
        when(mapper.toResponseDTO(product)).thenReturn(responseDTO);

        // Act
        ProductResponseDTO resultado = service.findById(1L);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao buscar id inexistente")
    void deveLancarExcecaoQuandoIdNaoEncontradoNoBuscar() {
        // Arrange
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Produto não encontrado");
    }

    @Test
    @DisplayName("Deve atualizar produto com sucesso")
    void deveAtualizarProdutoComSucesso() {
        // Arrange
        ProductUpdateDTO updateDTO = new ProductUpdateDTO("Teclado Atualizado", 349.90, 30);
        when(repository.findById(1L)).thenReturn(Optional.of(product));
        when(repository.save(any(Product.class))).thenReturn(product);
        when(mapper.toResponseDTO(product)).thenReturn(
                new ProductResponseDTO(1L, "Teclado Atualizado", 349.90, 30)
        );

        // Act
        ProductResponseDTO resultado = service.update(1L, updateDTO);

        // Assert
        assertThat(resultado.name()).isEqualTo("Teclado Atualizado");
        assertThat(resultado.price()).isEqualTo(349.90);
        verify(repository).save(any(Product.class));
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao atualizar id inexistente")
    void deveLancarExcecaoQuandoIdNaoEncontradoNoAtualizar() {
        // Arrange
        ProductUpdateDTO updateDTO = new ProductUpdateDTO("Teclado Atualizado", 349.90, 30);
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> service.update(99L, updateDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Produto não encontrado");

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Deve deletar produto com sucesso")
    void deveDeletarProdutoComSucesso() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(product));

        // Act
        service.delete(1L);

        // Assert
        verify(repository).delete(product);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao deletar id inexistente")
    void deveLancarExcecaoQuandoIdNaoEncontradoNoDeletar() {
        // Arrange
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> service.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Produto não encontrado");

        verify(repository, never()).delete(any());
    }
}