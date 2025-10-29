package dev.matheuslf.desafio.inscritos.integration;

import dev.matheuslf.desafio.inscritos.dto.request.LoginRequest;
import dev.matheuslf.desafio.inscritos.dto.request.RegisterUserRequest;
import dev.matheuslf.desafio.inscritos.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import utils.IntegrationTestBase;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AuthIntegrationTest extends IntegrationTestBase {

    @Test
    void deveRegistrarUsuarioComSucesso() throws Exception {
        var request = new RegisterUserRequest(
                "Usuário Teste",
                "teste@email.com",
                "123456"
        );

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Usuário Teste"))
                .andExpect(jsonPath("$.email").value("teste@email.com"));
    }

    @Test
    void deveRetornar409_QuandoRegistrarUsuarioComEmailExistente() throws Exception {
        userRepository.save(new User("Usuário Teste",
                "duplicado@email.com",
                "123456"));

        var request2 = new RegisterUserRequest(
                "Outro Usuário",
                "duplicado@email.com",
                "abcdef"
        );

        mockMvc.perform(
                        post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request2))
                ).andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Usuário já cadastrado com este e-mail."));
    }

    @Test
    void deveLogarComSucessoERetornarToken() throws Exception {
        var request = new LoginRequest("email@email.com", "123456");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void deveRetornar401_QuandoSenhaEstiverIncorreta() throws Exception {
        var request = new LoginRequest("email@email.com", "senhaErrada");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deveRetornar401_QuandoEmailNaoExistir() throws Exception {
        var request = new LoginRequest("naoexiste@email.com", "123456");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deveRetornar400_QuandoDadosDeLoginForemInvalidos() throws Exception {
        var request = new LoginRequest("", "");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400_QuandoDadosDeRegistroForemInvalidos() throws Exception {
        var request = new RegisterUserRequest("", "email", "");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}