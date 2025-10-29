package dev.matheuslf.desafio.inscritos.service;

import dev.matheuslf.desafio.inscritos.dto.request.LoginRequest;
import dev.matheuslf.desafio.inscritos.dto.request.RegisterUserRequest;
import dev.matheuslf.desafio.inscritos.dto.response.LoginResponse;
import dev.matheuslf.desafio.inscritos.dto.response.RegisterUserResponse;
import dev.matheuslf.desafio.inscritos.exception.ExistentUserException;
import dev.matheuslf.desafio.inscritos.mapper.AuthMapper;
import dev.matheuslf.desafio.inscritos.model.User;
import dev.matheuslf.desafio.inscritos.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private AuthMapper authMapper;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private TokenService tokenService;
    @Mock private Authentication authentication;

    @InjectMocks private AuthService authService;

    private User user;

    @BeforeEach
    void setup() {
        user = new User("Usuário Teste", "teste@email.com", "senha-criptografada");
        user.setId(1L);
    }

    @Test
    void deveRegistrarUsuarioComSucesso() {
        // Arrange
        var request = new RegisterUserRequest("Usuário Teste", "teste@email.com", "123456");
        var encodedPassword = "senha-criptografada";
        var userToSave = new User("Usuário Teste", "teste@email.com", encodedPassword);
        var response = new RegisterUserResponse( "Usuário Teste", "teste@email.com");

        when(userRepository.findUserByEmail(request.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.password())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(authMapper.toResponse(user)).thenReturn(response);

        // Act
        var result = authService.register(request);

        // Assert
        assertNotNull(result);
        assertEquals(response.name(), result.name());
        assertEquals(response.email(), result.email());
        verify(userRepository).findUserByEmail(request.email());
        verify(passwordEncoder).encode(request.password());
        verify(userRepository).save(any(User.class));
        verify(authMapper).toResponse(user);
    }

    @Test
    void deveLancarExcecaoAoRegistrarUsuarioComEmailExistente() {
        // Arrange
        var request = new RegisterUserRequest("Usuário Teste", "teste@email.com", "123456");
        when(userRepository.findUserByEmail(request.email())).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(ExistentUserException.class, () -> authService.register(request));
        verify(userRepository).findUserByEmail(request.email());
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void deveLogarUsuarioComSucesso() {
        // Arrange
        var request = new LoginRequest("teste@email.com", "123456");
        var token = "jwt-token";
        var expectedResponse = new LoginResponse(token);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(tokenService.generateToken(user)).thenReturn(token);
        when(authMapper.toResponse(token)).thenReturn(expectedResponse);

        // Act
        var result = authService.login(request);

        // Assert
        assertNotNull(result);
        assertEquals(token, result.token());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenService).generateToken(user);
        verify(authMapper).toResponse(token);
    }

    @Test
    void deveLancarExcecaoAoFalharLogin() {
        // Arrange
        var request = new LoginRequest("email@teste.com", "senhaErrada");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Bad Credentials"));

        // Act & Assert
        assertThrows(RuntimeException.class,
                () -> authService.login(request));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenService, never()).generateToken(any());
    }
}