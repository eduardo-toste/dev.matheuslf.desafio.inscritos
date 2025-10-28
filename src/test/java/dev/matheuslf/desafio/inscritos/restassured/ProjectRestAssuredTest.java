package dev.matheuslf.desafio.inscritos.restassured;

import dev.matheuslf.desafio.inscritos.dto.request.CreateProjectRequest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import utils.RestAssuredTestBase;

import java.time.Instant;
import java.util.Date;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ProjectRestAssuredTest extends RestAssuredTestBase {

    @Test
    void deveCriarProjetoComSucesso_QuandoDadosValidos() {
        var request = new CreateProjectRequest(
                "Projeto Teste",
                "Descrição do projeto",
                Date.from(Instant.now()),
                Date.from(Instant.now().plusSeconds(86400))
        );

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/projects")
                .then()
                .statusCode(201);
    }

    @Test
    void deveRetornar400_QuandoCriarProjetoComDadosInvalidos() {
        var request = new CreateProjectRequest(
                "",
                "Descrição inválida",
                Date.from(Instant.now()),
                Date.from(Instant.now().plusSeconds(86400))
        );

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/projects")
                .then()
                .statusCode(400);
    }

    @Test
    void deveBuscarProjetosComSucesso() {
        projectRepository.saveAll(
                java.util.List.of(
                        new dev.matheuslf.desafio.inscritos.model.Project("Projeto 1", "Descrição 1", new Date(), new Date()),
                        new dev.matheuslf.desafio.inscritos.model.Project("Projeto 2", "Descrição 2", new Date(), new Date())
                )
        );

        given()
                .when()
                .get("/projects")
                .then()
                .statusCode(200)
                .body("content.size()", is(2))
                .body("totalElements", equalTo(2));
    }

    @Test
    void deveRetornarListaVazia_QuandoNenhumProjetoEncontrado() {
        given()
                .when()
                .get("/projects")
                .then()
                .statusCode(200)
                .body("content.size()", equalTo(0))
                .body("totalElements", equalTo(0));
    }
}