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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository, TaskMapper taskMapper) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    public Long criarTask(CreateTaskRequest request) {
        Project projeto = projectRepository.findById(request.projectId())
                .orElseThrow(ProjectNotFoundException::new);

        Task task = taskRepository.save(taskMapper.toEntity(request, projeto));
        return task.getId();
    }

    public Page<TaskResponse> buscarTasks(Pageable pageable, Status status, Priority priority, Long projectId) {
        return taskRepository.findByFilters(pageable, status, priority, projectId)
                .map(taskMapper::toResponse);
    }

    public TaskResponse atualizarStatus(Long id, UpdateTaskStatusRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(TaskNotFoundException::new);

        task.setStatus(request.status());
        return taskMapper.toResponse(taskRepository.save(task));
    }


    public void excluirTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(TaskNotFoundException::new);

        taskRepository.delete(task);
    }
}
