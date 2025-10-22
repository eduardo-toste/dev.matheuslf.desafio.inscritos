package dev.matheuslf.desafio.inscritos.controller;

import dev.matheuslf.desafio.inscritos.dto.CreateProjectRequest;
import dev.matheuslf.desafio.inscritos.dto.ProjectResponse;
import dev.matheuslf.desafio.inscritos.repository.ProjectRepository;
import dev.matheuslf.desafio.inscritos.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<Void> criarProjeto(@RequestBody @Valid CreateProjectRequest request) {
        projectService.criarProjeto(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<Page<ProjectResponse>> buscarProjetos(Pageable pageable) {
        Page<ProjectResponse> projetos = projectService.buscarProjetos(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(projetos);
    }

}
