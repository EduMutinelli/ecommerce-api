package com.eduardo.ecommerce.dto;

public record AuthResponseDTO(
        String accessToken,
        String refreshToken
) {}