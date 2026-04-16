package com.eduardo.ecommerce.dto;

import com.eduardo.ecommerce.model.Role;

public record UserResponseDTO(
        Long id,
        String name,
        String email,
        Role role
) {}