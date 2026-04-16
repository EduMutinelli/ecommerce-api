package com.eduardo.ecommerce.mapper;

import com.eduardo.ecommerce.dto.ProductCreateDTO;
import com.eduardo.ecommerce.dto.ProductResponseDTO;
import com.eduardo.ecommerce.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(ProductCreateDTO dto) {
        Product product = new Product();
        product.setName(dto.name());
        product.setPrice(dto.price());
        product.setQuantity(dto.quantity());
        return product;
    }

    public ProductResponseDTO toResponseDTO(Product product) {
        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getQuantity()
        );
    }
}