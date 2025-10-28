package dev.matheuslf.desafio.inscritos.controller;

import dev.matheuslf.desafio.inscritos.dto.request.CreateTaskRequest;
import dev.matheuslf.desafio.inscritos.dto.response.TaskResponse;
import dev.matheuslf.desafio.inscritos.dto.request.UpdateTaskStatusRequest;
import dev.matheuslf.desafio.inscritos.model.enums.Priority;
import dev.matheuslf.desafio.inscritos.model.enums.Status;
import dev.matheuslf.desafio.inscritos.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/tasks")
@Tag(name = "Tarefas", description = "Gerenciamento de tarefas")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Criar nova tarefa", description = "Cria uma nova tarefa associada a um projeto.")
    @ApiResponse(responseCode = "201", description = "Tarefa criada com sucesso")
    @PostMapping
    public ResponseEntity<Void> criarTask(@RequestBody @Valid CreateTaskRequest request) {
        Long id = taskService.criarTask(request);
        return ResponseEntity.created(URI.create("/tasks/" + id)).build();
    }

    @Operation(summary = "Buscar tarefas",
            description = "Retorna uma lista paginada de tarefas com filtros opcionais de status, prioridade e ID do projeto.")
    @ApiResponse(responseCode = "200", description = "Lista de tarefas retornada com sucesso")
    @GetMapping
    public ResponseEntity<Page<TaskResponse>> buscarTasks(Pageable pageable,
                                                          @RequestParam(required = false) Status status,
                                                          @RequestParam(required = false) Priority priority,
                                                          @RequestParam(required = false) Long projectId) {
        Page<TaskResponse> tasks = taskService.buscarTasks(pageable, status, priority, projectId);
        return ResponseEntity.ok().body(tasks);
    }

    @Operation(summary = "Atualizar status da tarefa",
            description = "Atualiza o status de uma tarefa existente.")
    @ApiResponse(responseCode = "200", description = "Status da tarefa atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content)
    @ApiResponse(responseCode = "404", description = "Tarefa não encontrada", content = @Content)
    @PutMapping("/{id}/status")
    public ResponseEntity<TaskResponse> atualizarStatus(@PathVariable Long id, @RequestBody @Valid UpdateTaskStatusRequest request) {
        TaskResponse taskAtualizada = taskService.atualizarStatus(id, request);
        return ResponseEntity.ok().body(taskAtualizada);
    }

    @Operation(summary = "Excluir tarefa",
            description = "Exclui uma tarefa existente pelo ID.")
    @ApiResponse(responseCode = "200", description = "Tarefa excluída com sucesso")
    @ApiResponse(responseCode = "404", description = "Tarefa não encontrada", content = @Content)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirTask(@PathVariable Long id) {
        taskService.excluirTask(id);
        return ResponseEntity.ok().build();
    }

}
