package dev.matheuslf.desafio.inscritos.service;

import dev.matheuslf.desafio.inscritos.config.JWTUserData;
import dev.matheuslf.desafio.inscritos.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TokenServiceTest {

    private TokenService tokenService;
    private User user;

    @BeforeEach
    void setUp() {
        tokenService = new TokenService();
        user = new User("Usuário Teste", "teste@email.com", "senha123");
        user.setId(1L);
    }

    @Test
    void deveGerarTokenValido() {
        String token = tokenService.generateToken(user);

        assertNotNull(token);
        assertTrue(token.startsWith("ey"));
    }

    @Test
    void deveValidarTokenCorretamente() {
        String token = tokenService.generateToken(user);

        Optional<JWTUserData> result = tokenService.validateToken(token);

        assertTrue(result.isPresent());
        assertEquals(user.getId(), result.get().userId());
        assertEquals(user.getEmail(), result.get().email());
    }

    @Test
    void deveRetornarEmptyParaTokenInvalido() {
        String tokenInvalido = "token.falso.invalido";

        Optional<JWTUserData> result = tokenService.validateToken(tokenInvalido);

        assertTrue(result.isEmpty());
    }
}