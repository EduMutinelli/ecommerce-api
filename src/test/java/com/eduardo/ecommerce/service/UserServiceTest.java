package com.eduardo.ecommerce.service;

import com.eduardo.ecommerce.dto.UserCreateDTO;
import com.eduardo.ecommerce.dto.UserResponseDTO;
import com.eduardo.ecommerce.dto.UserUpdateDTO;
import com.eduardo.ecommerce.exception.BusinessException;
import com.eduardo.ecommerce.exception.ResourceNotFoundException;
import com.eduardo.ecommerce.mapper.UserMapper;
import com.eduardo.ecommerce.model.Role;
import com.eduardo.ecommerce.model.User;
import com.eduardo.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UserService service;

    private User user;
    private UserResponseDTO responseDTO;
    private UserCreateDTO createDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setName("Eduardo");
        user.setEmail("edu@email.com");
        user.setPassword("senha_encoded");
        user.setRole(Role.ROLE_USER);

        createDTO = new UserCreateDTO(
                "Eduardo",
                "edu@email.com",
                "123456",
                Role.ROLE_USER
        );

        responseDTO = new UserResponseDTO(1L, "Eduardo", "edu@email.com", Role.ROLE_USER);
    }

    @Test
    @DisplayName("Deve criar usuário com sucesso")
    void deveCriarUsuarioComSucesso() {
        // Arrange
        when(repository.existsByEmail(createDTO.email())).thenReturn(false);
        when(mapper.toEntity(createDTO)).thenReturn(user);
        when(passwordEncoder.encode(createDTO.password())).thenReturn("senha_encoded");
        when(repository.save(any(User.class))).thenReturn(user);
        when(mapper.toResponseDTO(user)).thenReturn(responseDTO);

        // Act
        UserResponseDTO resultado = service.create(createDTO);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.name()).isEqualTo("Eduardo");
        assertThat(resultado.email()).isEqualTo("edu@email.com");
        verify(repository).save(any(User.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException quando email já cadastrado")
    void deveLancarExcecaoQuandoEmailDuplicado() {
        // Arrange
        when(repository.existsByEmail(createDTO.email())).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> service.create(createDTO))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Email já cadastrado");

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Deve retornar lista de usuários")
    void deveRetornarListaDeUsuarios() {
        // Arrange
        when(repository.findAll()).thenReturn(List.of(user));
        when(mapper.toResponseDTO(user)).thenReturn(responseDTO);

        // Act
        List<UserResponseDTO> resultado = service.findAll();

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).email()).isEqualTo("edu@email.com");
    }

    @Test
    @DisplayName("Deve retornar usuário por id com sucesso")
    void deveRetornarUsuarioPorId() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(user));
        when(mapper.toResponseDTO(user)).thenReturn(responseDTO);

        // Act
        UserResponseDTO resultado = service.findById(1L);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao buscar id inexistente")
    void deveLancarExcecaoQuandoIdNaoEncontradoNoBuscar() {
        // Arrange
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Usuário não encontrado");
    }

    @Test
    @DisplayName("Deve atualizar usuário com sucesso")
    void deveAtualizarUsuarioComSucesso() {
        // Arrange
        UserUpdateDTO updateDTO = new UserUpdateDTO("Eduardo Atualizado", "novaSenha");
        when(repository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("novaSenha")).thenReturn("novaSenha_encoded");
        when(repository.save(any(User.class))).thenReturn(user);
        when(mapper.toResponseDTO(user)).thenReturn(
                new UserResponseDTO(1L, "Eduardo Atualizado", "edu@email.com", Role.ROLE_USER)
        );

        // Act
        UserResponseDTO resultado = service.update(1L, updateDTO);

        // Assert
        assertThat(resultado.name()).isEqualTo("Eduardo Atualizado");
        verify(passwordEncoder).encode("novaSenha");
        verify(repository).save(any(User.class));
    }

    @Test
    @DisplayName("Deve deletar usuário com sucesso")
    void deveDeletarUsuarioComSucesso() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        service.delete(1L);

        // Assert
        verify(repository).delete(user);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao deletar id inexistente")
    void deveLancarExcecaoQuandoIdNaoEncontradoNoDeletar() {
        // Arrange
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> service.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Usuário não encontrado");

        verify(repository, never()).delete(any());
    }
}