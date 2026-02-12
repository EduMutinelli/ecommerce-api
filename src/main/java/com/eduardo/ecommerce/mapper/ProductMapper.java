package com.eduardo.ecommerce.mapper;

import com.eduardo.ecommerce.dto.ProductDTO;
import com.eduardo.ecommerce.model.Product;

public class ProductMapper {

    private ProductMapper()
    {}
    public static Product toEntity(ProductDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());
        return product;
    }

    public static ProductDTO toDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setQuantity(product.getQuantity());
        return dto;
    }
}
