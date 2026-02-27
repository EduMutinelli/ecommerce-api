package com.eduardo.ecommerce.service;

import com.eduardo.ecommerce.dto.UserCreateDTO;
import com.eduardo.ecommerce.dto.UserResponseDTO;
import com.eduardo.ecommerce.exception.ResourceNotFoundException;
import com.eduardo.ecommerce.mapper.UserMapper;
import com.eduardo.ecommerce.model.User;
import com.eduardo.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) { this.repository = repository; }

    public UserResponseDTO create(UserCreateDTO dto) {
        User user = UserMapper.toEntity(dto);
        User saved = repository.save(user);
        return UserMapper.toResponseDTO(saved);
    }

    public List<UserResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(UserMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public UserResponseDTO findById(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        return UserMapper.toResponseDTO(user);
    }

    public void delete(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        repository.delete(user);
    }
}
