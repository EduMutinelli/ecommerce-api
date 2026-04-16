package com.eduardo.ecommerce.mapper;

import com.eduardo.ecommerce.dto.UserCreateDTO;
import com.eduardo.ecommerce.dto.UserResponseDTO;
import com.eduardo.ecommerce.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserCreateDTO dto) {
        User user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setRole(dto.role());
        // senha não é mapeada aqui — responsabilidade do UserService
        return user;
    }

    public UserResponseDTO toResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }
}