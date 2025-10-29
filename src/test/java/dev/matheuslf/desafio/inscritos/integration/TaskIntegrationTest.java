package dev.matheuslf.desafio.inscritos.integration;

import dev.matheuslf.desafio.inscritos.dto.request.CreateTaskRequest;
import dev.matheuslf.desafio.inscritos.dto.request.UpdateTaskStatusRequest;
import dev.matheuslf.desafio.inscritos.model.Project;
import dev.matheuslf.desafio.inscritos.model.Task;
import dev.matheuslf.desafio.inscritos.model.enums.Priority;
import dev.matheuslf.desafio.inscritos.model.enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import utils.IntegrationTestBase;

import java.time.Instant;
import java.util.Date;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TaskIntegrationTest extends IntegrationTestBase {

    private Project projeto;

    @BeforeEach
    void setupProjeto() {
        projeto = projectRepository.save(new Project(
                "Projeto Teste",
                "Descrição de projeto teste",
                Date.from(Instant.now()),
                Date.from(Instant.now().plusSeconds(900000))
        ));
    }

    @Test
    void deveCriarTaskComSucesso_QuandoDadosValidos() throws Exception {
        var request = new CreateTaskRequest(
                "Task Teste",
                "Descrição de task teste",
                Priority.MEDIUM,
                Date.from(Instant.now().plusSeconds(900000)),
                projeto.getId()
        );

        mockMvc.perform(post("/tasks")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void deveRetornar400_AoCriarTaskComDadosInvalidos() throws Exception {
        var request = new CreateTaskRequest(
                "",
                "Descrição de task teste",
                Priority.MEDIUM,
                Date.from(Instant.now().plusSeconds(900000)),
                projeto.getId()
        );

        mockMvc.perform(post("/tasks")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar404_AoCriarTask_QuandoProjetoNaoEncontrado() throws Exception {
        var request = new CreateTaskRequest(
                "Task Teste",
                "Descrição de task teste",
                Priority.MEDIUM,
                Date.from(Instant.now().plusSeconds(900000)),
                2L
        );

        mockMvc.perform(post("/tasks")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveBuscarTasksCadastradasSemFiltroComSucesso() throws Exception {
        taskRepository.save(new Task("Task 1", "Descrição 1", Status.TODO, Priority.MEDIUM, new Date(), projeto));
        taskRepository.save(new Task("Task 2", "Descrição 2", Status.TODO, Priority.MEDIUM, new Date(), projeto));

        mockMvc.perform(get("/tasks")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2));
    }

    @Test
    void deveBuscarTasksComFiltroStatusComSucesso() throws Exception {
        taskRepository.save(new Task("Task A", "desc", Status.TODO, Priority.LOW, new Date(), projeto));
        taskRepository.save(new Task("Task B", "desc", Status.DONE, Priority.LOW, new Date(), projeto));

        mockMvc.perform(get("/tasks")
                        .header("Authorization", "Bearer " + token)
                        .param("status", "TODO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].status").value("TODO"));
    }

    @Test
    void deveBuscarTasksComFiltroPriorityComSucesso() throws Exception {
        taskRepository.save(new Task("Task A", "desc", Status.TODO, Priority.HIGH, new Date(), projeto));
        taskRepository.save(new Task("Task B", "desc", Status.TODO, Priority.LOW, new Date(), projeto));

        mockMvc.perform(get("/tasks")
                        .header("Authorization", "Bearer " + token)
                        .param("priority", "HIGH"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].priority").value("HIGH"));
    }

    @Test
    void deveBuscarTasksComFiltroProjectIdComSucesso() throws Exception {
        Project outroProjeto = projectRepository.save(new Project("Outro", "desc", new Date(), null));
        taskRepository.save(new Task("Task A", "desc", Status.TODO, Priority.HIGH, new Date(), projeto));
        taskRepository.save(new Task("Task B", "desc", Status.TODO, Priority.HIGH, new Date(), outroProjeto));

        mockMvc.perform(get("/tasks")
                        .header("Authorization", "Bearer " + token)
                        .param("projectId", projeto.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    void deveBuscarTasksComTodosFiltrosComSucesso() throws Exception {
        taskRepository.save(new Task("Task A", "desc", Status.TODO, Priority.LOW, new Date(), projeto));
        taskRepository.save(new Task("Task B", "desc", Status.DONE, Priority.HIGH, new Date(), projeto));

        mockMvc.perform(get("/tasks")
                        .header("Authorization", "Bearer " + token)
                        .param("status", "TODO")
                        .param("priority", "LOW")
                        .param("projectId", projeto.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    void deveRetornarListaVazia_QuandoNenhumaTaskEncontrada() throws Exception {
        mockMvc.perform(get("/tasks")
                        .header("Authorization", "Bearer " + token)
                        .param("status", "DONE")
                        .param("priority", "HIGH")
                        .param("projectId", "999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(0));
    }

    @Test
    void deveRetornarTasksPaginadasComSucesso() throws Exception {
        IntStream.rangeClosed(1, 30).forEach(i ->
                taskRepository.save(new Task("Task " + i, "desc", Status.TODO, Priority.MEDIUM, new Date(), projeto))
        );

        mockMvc.perform(get("/tasks")
                        .header("Authorization", "Bearer " + token)
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(10));
    }

    @Test
    void deveAtualizarStatusTaskComSucesso() throws Exception {
        Task task = taskRepository.save(new Task(
                "Task A", "desc", Status.TODO, Priority.LOW, new Date(), projeto
        ));

        var request = new UpdateTaskStatusRequest(Status.DOING);

        mockMvc.perform(put("/tasks/{id}/status", task.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DOING"));
    }

    @Test
    void deveRetornar400_AtualizarStatusTask_QuandoDadosInvalidos() throws Exception {
        Task task = taskRepository.save(new Task(
                "Task A", "desc", Status.TODO, Priority.LOW, new Date(), projeto
        ));

        var request = new UpdateTaskStatusRequest(null);

        mockMvc.perform(put("/tasks/{id}/status", task.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar404_AtualizarStatusTask_QuandoTaskNaoEncontrada() throws Exception {
        var request = new UpdateTaskStatusRequest(Status.DOING);

        mockMvc.perform(put("/tasks/{id}/status", 1L)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveExcluirTaskComSucesso() throws Exception {
        Task task = taskRepository.save(new Task(
                "Task A", "desc", Status.TODO, Priority.LOW, new Date(), projeto
        ));

        mockMvc.perform(delete("/tasks/{id}", task.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void deveRetornar404_AoExcluirTask_QuandoTaskNaoEncontrada() throws Exception {
        mockMvc.perform(delete("/tasks/{id}", 1L)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }
}