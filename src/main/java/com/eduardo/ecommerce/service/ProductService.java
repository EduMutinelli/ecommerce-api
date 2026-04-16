package com.eduardo.ecommerce.service;

import com.eduardo.ecommerce.dto.ProductCreateDTO;
import com.eduardo.ecommerce.dto.ProductResponseDTO;
import com.eduardo.ecommerce.dto.ProductUpdateDTO;
import com.eduardo.ecommerce.exception.ResourceNotFoundException;
import com.eduardo.ecommerce.mapper.ProductMapper;
import com.eduardo.ecommerce.model.Product;
import com.eduardo.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    public ProductService(ProductRepository repository, ProductMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public ProductResponseDTO create(ProductCreateDTO dto) {
        return mapper.toResponseDTO(repository.save(mapper.toEntity(dto)));
    }

    public List<ProductResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    public ProductResponseDTO findById(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
        return mapper.toResponseDTO(product);
    }

    public ProductResponseDTO update(Long id, ProductUpdateDTO dto) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        product.setName(dto.name());
        product.setPrice(dto.price());
        product.setQuantity(dto.quantity());

        return mapper.toResponseDTO(repository.save(product));
    }

    public void delete(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
        repository.delete(product);
    }
}