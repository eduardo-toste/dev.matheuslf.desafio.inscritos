package dev.matheuslf.desafio.inscritos.restassured;

import dev.matheuslf.desafio.inscritos.dto.CreateTaskRequest;
import dev.matheuslf.desafio.inscritos.dto.UpdateTaskStatusRequest;
import dev.matheuslf.desafio.inscritos.model.Project;
import dev.matheuslf.desafio.inscritos.model.Task;
import dev.matheuslf.desafio.inscritos.model.enums.Priority;
import dev.matheuslf.desafio.inscritos.model.enums.Status;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.RestAssuredTestBase;

import java.time.Instant;
import java.util.Date;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class TaskRestAssuredTest extends RestAssuredTestBase {

    private Project projeto;

    @BeforeEach
    void init() {
        projeto = projectRepository.save(new Project(
                "Projeto Tarefa",
                "Projeto para testar tarefas",
                new Date(),
                new Date()
        ));
    }

    @Test
    void deveCriarTaskComSucesso_QuandoDadosValidos() {
        var request = new CreateTaskRequest(
                "Task 1",
                "Descrição da task",
                Priority.MEDIUM,
                Date.from(Instant.now().plusSeconds(86400)),
                projeto.getId()
        );

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/tasks")
                .then()
                .statusCode(201);
    }

    @Test
    void deveRetornar400_QuandoCriarTaskComDadosInvalidos() {
        var request = new CreateTaskRequest(
                "",
                "Descrição da task",
                Priority.MEDIUM,
                Date.from(Instant.now().plusSeconds(86400)),
                projeto.getId()
        );

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/tasks")
                .then()
                .statusCode(400);
    }

    @Test
    void deveAtualizarStatusDaTaskComSucesso() {
        var task = taskRepository.save(new Task(
                "Task Atualizável",
                "Status inicial",
                Status.TODO,
                Priority.LOW,
                new Date(),
                projeto
        ));

        var request = new UpdateTaskStatusRequest(Status.DONE);

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .put("/tasks/{id}/status", task.getId())
                .then()
                .statusCode(200)
                .body("status", equalTo("DONE"));
    }

    @Test
    void deveRetornar404_QuandoAtualizarTaskInexistente() {
        var request = new UpdateTaskStatusRequest(Status.DONE);

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .put("/tasks/999/status")
                .then()
                .statusCode(404);
    }

    @Test
    void deveBuscarTasksPorStatusComSucesso() {
        taskRepository.save(new Task("Task A", "desc", Status.TODO, Priority.MEDIUM, new Date(), projeto));
        taskRepository.save(new Task("Task B", "desc", Status.DONE, Priority.MEDIUM, new Date(), projeto));

        given()
                .queryParam("status", "TODO")
                .when()
                .get("/tasks")
                .then()
                .statusCode(200)
                .body("content.size()", equalTo(1))
                .body("content[0].status", equalTo("TODO"));
    }

    @Test
    void deveExcluirTaskComSucesso() {
        var task = taskRepository.save(new Task("Task a excluir", "desc", Status.TODO, Priority.LOW, new Date(), projeto));

        given()
                .when()
                .delete("/tasks/{id}", task.getId())
                .then()
                .statusCode(200);
    }

    @Test
    void deveRetornar404_QuandoExcluirTaskInexistente() {
        given()
                .when()
                .delete("/tasks/{id}", 999L)
                .then()
                .statusCode(404);
    }
}