package com.eduardo.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthRefreshDTO(
        @NotBlank(message = "Refresh token é obrigatório")
        String refreshToken
) {}