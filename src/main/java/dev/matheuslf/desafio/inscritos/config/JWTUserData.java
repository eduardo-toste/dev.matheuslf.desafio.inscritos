package dev.matheuslf.desafio.inscritos.config;

import dev.matheuslf.desafio.inscritos.dto.response.LoginResponse;

public record JWTUserData(

        Long userId,
        String email

) {
}
