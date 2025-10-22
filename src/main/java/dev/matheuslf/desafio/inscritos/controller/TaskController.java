package dev.matheuslf.desafio.inscritos.controller;

import dev.matheuslf.desafio.inscritos.dto.CreateTaskRequest;
import dev.matheuslf.desafio.inscritos.dto.TaskResponse;
import dev.matheuslf.desafio.inscritos.dto.UpdateTaskStatusRequest;
import dev.matheuslf.desafio.inscritos.model.enums.Priority;
import dev.matheuslf.desafio.inscritos.model.enums.Status;
import dev.matheuslf.desafio.inscritos.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<Void> criarTask(@RequestBody @Valid CreateTaskRequest request) {
        Long id = taskService.criarTask(request);
        return ResponseEntity.created(URI.create("/tasks/" + id)).build();
    }

    @GetMapping
    public ResponseEntity<Page<TaskResponse>> buscarTasks(Pageable pageable,
                                                          @RequestParam(required = false) Status status,
                                                          @RequestParam(required = false) Priority priority,
                                                          @RequestParam(required = false) Long projectId) {
        Page<TaskResponse> tasks = taskService.buscarTasks(pageable, status, priority, projectId);
        return ResponseEntity.ok().body(tasks);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<TaskResponse> atualizarStatus(@PathVariable Long id, @RequestBody UpdateTaskStatusRequest request) {
        TaskResponse taskAtualizada = taskService.atualizarStatus(id, request);
        return ResponseEntity.ok().body(taskAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirTask(@PathVariable Long id) {
        taskService.excluirTask(id);
        return ResponseEntity.ok().build();
    }

}
