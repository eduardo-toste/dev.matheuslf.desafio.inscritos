package dev.matheuslf.desafio.inscritos.restassured;

import dev.matheuslf.desafio.inscritos.dto.request.LoginRequest;
import dev.matheuslf.desafio.inscritos.dto.request.RegisterUserRequest;
import dev.matheuslf.desafio.inscritos.model.User;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import utils.RestAssuredTestBase;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class AuthRestAssuredTest extends RestAssuredTestBase {

    @Test
    void deveRegistrarUsuarioComSucesso() {
        var request = new RegisterUserRequest(
                "Usuário Teste",
                "teste@email.com",
                "123456"
        );

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/auth/register")
                .then()
                .statusCode(200)
                .body("name", equalTo("Usuário Teste"))
                .body("email", equalTo("teste@email.com"));
    }

    @Test
    void deveRetornar409_QuandoRegistrarUsuarioComEmailExistente() {
        userRepository.save(new User(
                "Usuário Teste",
                "duplicado@email.com",
                "123456"
        ));

        var request = new RegisterUserRequest(
                "Outro Usuário",
                "duplicado@email.com",
                "abcdef"
        );

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/auth/register")
                .then()
                .statusCode(409)
                .body("message", equalTo("Usuário já cadastrado com este e-mail."));
    }

    @Test
    void deveLogarComSucessoERetornarToken() {
        userRepository.save(new User(
                "Usuário Existente",
                "email@email.com",
                passwordEncoder.encode("123456")
        ));

        var request = new LoginRequest("email@email.com", "123456");

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(201)
                .body("token", not(emptyOrNullString()));
    }

    @Test
    void deveRetornar401_QuandoSenhaEstiverIncorreta() {
        userRepository.save(new User(
                "Usuário Existente",
                "email@email.com",
                passwordEncoder.encode("123456")
        ));

        var request = new LoginRequest("email@email.com", "senhaErrada");

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(401);
    }

    @Test
    void deveRetornar401_QuandoEmailNaoExistir() {
        var request = new LoginRequest("naoexiste@email.com", "123456");

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(401);
    }

    @Test
    void deveRetornar400_QuandoDadosDeLoginForemInvalidos() {
        var request = new LoginRequest("", "");

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(400);
    }

    @Test
    void deveRetornar400_QuandoDadosDeRegistroForemInvalidos() {
        var request = new RegisterUserRequest("", "email", "");

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/auth/register")
                .then()
                .statusCode(400);
    }
}