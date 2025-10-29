package dev.matheuslf.desafio.inscritos.controller;

import dev.matheuslf.desafio.inscritos.dto.request.LoginRequest;
import dev.matheuslf.desafio.inscritos.dto.request.RegisterUserRequest;
import dev.matheuslf.desafio.inscritos.dto.response.LoginResponse;
import dev.matheuslf.desafio.inscritos.dto.response.RegisterUserResponse;
import dev.matheuslf.desafio.inscritos.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Endpoints de autenticação e registro de usuários")
public class AuthController {

    private final AuthService userService;

    public AuthController(AuthService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Login do usuário",
            description = "Realiza o login do usuário e retorna o token JWT de autenticação."
    )
    @ApiResponse(responseCode = "201", description = "Login realizado com sucesso")
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas", content = @Content)
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        var response = userService.login(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Registro de novo usuário",
            description = "Registra um novo usuário no sistema."
    )
    @ApiResponse(responseCode = "200", description = "Usuário registrado com sucesso")
    @ApiResponse(responseCode = "409", description = "Usuário já existe com este e-mail", content = @Content)
    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponse> register(@RequestBody @Valid RegisterUserRequest request) {
        var response = userService.register(request);
        return ResponseEntity.ok(response);
    }
}