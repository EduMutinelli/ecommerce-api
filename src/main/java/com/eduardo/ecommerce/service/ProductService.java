package com.eduardo.ecommerce.service;

import com.eduardo.ecommerce.dto.ProductDTO;
import com.eduardo.ecommerce.exception.ResourceNotFoundException;
import com.eduardo.ecommerce.mapper.ProductMapper;
import com.eduardo.ecommerce.model.Product;
import com.eduardo.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public ProductDTO create(ProductDTO dto) {
        Product product = ProductMapper.toEntity(dto);
        Product saved = repository.save(product);
        return ProductMapper.toDTO(saved);
    }

    public List<ProductDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(ProductMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO findById(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
        return ProductMapper.toDTO(product);
    }

    public ProductDTO update(Long id, ProductDTO dto) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());

        Product updated = repository.save(product);
        return ProductMapper.toDTO(updated);
    }

    public void delete(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        repository.delete(product);
    }
}
