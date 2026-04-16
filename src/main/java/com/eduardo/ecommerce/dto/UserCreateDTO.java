package com.eduardo.ecommerce.dto;

import com.eduardo.ecommerce.model.Role;
import jakarta.validation.constraints.*;

public record UserCreateDTO(

        @NotBlank(message = "Nome é obrigatório")
        String name,

        @NotBlank(message = "E-mail é obrigatório")
        @Email(message = "E-mail inválido")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
        String password,

        @NotNull(message = "Escolha o tipo de usuário")
        Role role

) {}