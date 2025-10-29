package dev.matheuslf.desafio.inscritos.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import dev.matheuslf.desafio.inscritos.config.JWTUserData;
import dev.matheuslf.desafio.inscritos.model.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class TokenService {

    private final String SECRET = "secret";
    private final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET);

    public String generateToken(User user) {
        return JWT.create()
                .withClaim("userId", user.getId())
                .withSubject(user.getEmail())
                .withExpiresAt(Instant.now().plusSeconds(900000))
                .withIssuedAt(Instant.now())
                .sign(ALGORITHM);
    }

    public Optional<JWTUserData> validateToken(String token) {
        try {
            DecodedJWT decode = JWT.require(ALGORITHM).build().verify(token);
            return Optional.of(new JWTUserData(
                    decode.getClaim("userId").asLong(),
                    decode.getSubject()));
        } catch (JWTVerificationException ex) {
            return Optional.empty();
        }
    }
}
