package dev.matheuslf.desafio.inscritos.controller;

import dev.matheuslf.desafio.inscritos.dto.request.CreateProjectRequest;
import dev.matheuslf.desafio.inscritos.dto.response.ProjectResponse;
import dev.matheuslf.desafio.inscritos.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projects")
@Tag(name = "Projetos", description = "Gerenciamento de projetos")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Operation(summary = "Criar novo projeto",
            description = "Cria um novo projeto a partir dos dados fornecidos.")
    @ApiResponse(responseCode = "201", description = "Projeto criado com sucesso")
    @PostMapping
    public ResponseEntity<Void> criarProjeto(@RequestBody @Valid CreateProjectRequest request) {
        projectService.criarProjeto(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Buscar projetos",
            description = "Retorna uma lista paginada de projetos.")
    @ApiResponse(responseCode = "200", description = "Lista de projetos retornada com sucesso")
    @GetMapping
    public ResponseEntity<Page<ProjectResponse>> buscarProjetos(Pageable pageable) {
        Page<ProjectResponse> projetos = projectService.buscarProjetos(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(projetos);
    }

}
