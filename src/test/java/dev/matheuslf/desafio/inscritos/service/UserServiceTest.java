package dev.matheuslf.desafio.inscritos.service;

import dev.matheuslf.desafio.inscritos.exception.UserNotFoundException;
import dev.matheuslf.desafio.inscritos.model.User;
import dev.matheuslf.desafio.inscritos.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository userRepository;
    @InjectMocks private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("Usuário Teste", "email@email.com", "senha123");
        user.setId(1L);
    }

    @Test
    void deveRetornarUsuarioPorEmailComSucesso() {
        when(userRepository.findUserByEmail("email@email.com")).thenReturn(Optional.of(user));

        UserDetails result = userService.loadUserByUsername("email@email.com");

        assertNotNull(result);
        assertEquals(user.getEmail(), result.getUsername());
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        when(userRepository.findUserByEmail("naoexiste@email.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                userService.loadUserByUsername("naoexiste@email.com"));
    }
}