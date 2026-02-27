package com.eduardo.ecommerce.mapper;

import com.eduardo.ecommerce.dto.UserCreateDTO;
import com.eduardo.ecommerce.dto.UserResponseDTO;
import com.eduardo.ecommerce.model.User;

public class UserMapper {
    private UserMapper()
    {}
    public static User toEntity(UserCreateDTO dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole());
        return user;
    }

    public static UserResponseDTO toResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }

}
