package dev.matheuslf.desafio.inscritos.service;

import dev.matheuslf.desafio.inscritos.dto.CreateTaskRequest;
import dev.matheuslf.desafio.inscritos.dto.TaskResponse;
import dev.matheuslf.desafio.inscritos.dto.UpdateTaskStatusRequest;
import dev.matheuslf.desafio.inscritos.exception.ProjectNotFoundException;
import dev.matheuslf.desafio.inscritos.exception.TaskNotFoundException;
import dev.matheuslf.desafio.inscritos.mapper.TaskMapper;
import dev.matheuslf.desafio.inscritos.model.Project;
import dev.matheuslf.desafio.inscritos.model.Task;
import dev.matheuslf.desafio.inscritos.model.enums.Priority;
import dev.matheuslf.desafio.inscritos.model.enums.Status;
import dev.matheuslf.desafio.inscritos.repository.ProjectRepository;
import dev.matheuslf.desafio.inscritos.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock private ProjectRepository projectRepository;
    @Mock private TaskRepository taskRepository;
    @Mock private TaskMapper taskMapper;

    @InjectMocks private TaskService taskService;

    private Task task;
    private Project project;

    @BeforeEach
    void setup() {
        project = new Project(
                "Projeto Teste",
                "Projeto usado para teste",
                Date.from(Instant.now()),
                null
        );

        task = new Task(
                1L,
                "Task Teste",
                "Task usada para teste",
                Status.TODO,
                Priority.LOW,
                Date.from(Instant.now().plusSeconds(900000)),
                project
        );
    }

    @Test
    void deveCriarTaskComSucesso() {
        var request = new CreateTaskRequest(
                "Task Teste",
                "Task usada para teste",
                Priority.LOW,
                Date.from(Instant.now().plusSeconds(900000)),
                1L
        );
        when(projectRepository.findById(request.projectId())).thenReturn(Optional.of(project));
        when(taskMapper.toEntity(request, project)).thenReturn(task);
        when(taskRepository.save(any())).thenReturn(task);

        var result = taskService.criarTask(request);

        assertNotNull(result);
        assertEquals(1L, result);
        verify(projectRepository).findById(request.projectId());
        verify(taskMapper).toEntity(any(), any());
        verify(taskRepository).save(any());
    }

    @Test
    void deveLancarExcecao_AoCriarTask_QuandoProjetoNaoExistir() {
        var request = new CreateTaskRequest(
                "Task Teste",
                "Task usada para teste",
                Priority.LOW,
                Date.from(Instant.now().plusSeconds(900000)),
                1L
        );
        when(projectRepository.findById(request.projectId())).thenReturn(Optional.empty());

        var ex = assertThrows(ProjectNotFoundException.class,
                () -> taskService.criarTask(request));

        assertNotNull(ex);
        assertEquals("Projeto não encontrado.", ex.getMessage());
        verify(projectRepository).findById(request.projectId());
        verify(taskMapper, never()).toEntity(any(), any());
        verify(taskRepository, never()).save(any());
    }

    @Test
    void deveRetornarTodasAsTasksSemFiltroComSucesso() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Task> page = new PageImpl<>(List.of(task));
        when(taskRepository.findByFilters(pageable, null, null, null)).thenReturn(page);

        var result = taskService.buscarTasks(pageable, null, null, null);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(taskRepository).findByFilters(any(), any(), any(), any());
    }

    @Test
    void deveRetornarPageVaziaDeTasksComSucesso() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Task> page = new PageImpl<>(List.of());
        when(taskRepository.findByFilters(pageable, null, null, null)).thenReturn(page);

        var result = taskService.buscarTasks(pageable, null, null, null);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        verify(taskRepository).findByFilters(any(), any(), any(), any());
    }

    @Test
    void deveRetornarTodasAsTasksUsandoFiltroStatusComSucesso() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Task> page = new PageImpl<>(List.of(task));
        when(taskRepository.findByFilters(pageable, Status.TODO, null, null)).thenReturn(page);

        var result = taskService.buscarTasks(pageable, Status.TODO, null, null);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(taskRepository).findByFilters(any(), any(), any(), any());
    }

    @Test
    void deveRetornarTodasAsTasksUsandoFiltroPriorityComSucesso() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Task> page = new PageImpl<>(List.of(task));
        when(taskRepository.findByFilters(pageable, null, Priority.LOW, null)).thenReturn(page);

        var result = taskService.buscarTasks(pageable, null, Priority.LOW, null);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(taskRepository).findByFilters(any(), any(), any(), any());
    }

    @Test
    void deveRetornarTodasAsTasksUsandoFiltroProjectIdComSucesso() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Task> page = new PageImpl<>(List.of(task));
        when(taskRepository.findByFilters(pageable, null, null, 1L)).thenReturn(page);

        var result = taskService.buscarTasks(pageable, null, null, 1L);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(taskRepository).findByFilters(any(), any(), any(), any());
    }

    @Test
    void deveRetornarTodasAsTasksUsandoTodosFiltrosComSucesso() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Task> page = new PageImpl<>(List.of(task));
        when(taskRepository.findByFilters(pageable, Status.TODO, Priority.LOW, 1L)).thenReturn(page);

        var result = taskService.buscarTasks(pageable, Status.TODO, Priority.LOW, 1L);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(taskRepository).findByFilters(any(), any(), any(), any());
    }

    @Test
    void deveAtualizarStatusTaskComSucesso() {
        Long id = 1L;
        var request = new UpdateTaskStatusRequest(Status.DOING);
        task.setStatus(Status.DOING);
        TaskResponse response = new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getDueDate(),
                task.getProject().getId()
        );

        when(taskRepository.findById(id)).thenReturn(Optional.of(task));
        when(taskRepository.save(any())).thenReturn(task);
        when(taskMapper.toResponse(task)).thenReturn(response);

        var result = taskService.atualizarStatus(id, request);

        assertNotNull(result);
        assertEquals(Status.DOING, result.status());
        verify(taskRepository).findById(id);
        verify(taskRepository).save(task);
        verify(taskMapper).toResponse(task);
    }

    @Test
    void deveLancarExcecao_AoAtualizarStatusTask_QuandoTaskNaoExistir() {
        Long id = 1L;
        var request = new UpdateTaskStatusRequest(Status.DOING);

        when(taskRepository.findById(id)).thenReturn(Optional.empty());

        var ex = assertThrows(TaskNotFoundException.class,
                () -> taskService.atualizarStatus(id, request));

        assertNotNull(ex);
        assertEquals("Task não encontrada.", ex.getMessage());
        verify(taskRepository).findById(id);
        verify(taskMapper, never()).toResponse(task);
        verify(taskRepository, never()).save(task);
    }

    @Test
    void deveDeletarTaskComSucesso() {
        Long id = 1L;
        when(taskRepository.findById(id)).thenReturn(Optional.of(task));

        taskService.excluirTask(id);

        verify(taskRepository).findById(id);
        verify(taskRepository).delete(task);
    }

    @Test
    void deveLancarExcecao_AoDeletarTask_QuandoTaskNaoExistir() {
        Long id = 1L;
        when(taskRepository.findById(id)).thenReturn(Optional.empty());

        var ex = assertThrows(TaskNotFoundException.class,
                () -> taskService.excluirTask(id));

        assertEquals("Task não encontrada.", ex.getMessage());
        verify(taskRepository).findById(id);
        verify(taskRepository, never()).delete(task);
    }

}