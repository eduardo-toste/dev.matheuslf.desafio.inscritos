package dev.matheuslf.desafio.inscritos.service;

import dev.matheuslf.desafio.inscritos.dto.CreateProjectRequest;
import dev.matheuslf.desafio.inscritos.dto.ProjectResponse;
import dev.matheuslf.desafio.inscritos.mapper.ProjectMapper;
import dev.matheuslf.desafio.inscritos.model.Project;
import dev.matheuslf.desafio.inscritos.repository.ProjectRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock private ProjectRepository projectRepository;
    @Mock private ProjectMapper projectMapper;

    @InjectMocks private ProjectService projectService;

    private Project project;

    @BeforeEach
    void setup() {
        project = new Project(
                "Projeto Teste",
                "Projeto usado para teste",
                Date.from(Instant.now()),
                null
        );
    }

    @Test
    void deveCriarProjetoComSucesso() {
        var request = new CreateProjectRequest(
                "Projeto Teste",
                "Projeto usado para teste",
                Date.from(Instant.now()),
                null
        );
        when(projectMapper.toEntity(request)).thenReturn(project);

        projectService.criarProjeto(request);

        verify(projectMapper).toEntity(request);
        verify(projectRepository).save(project);
    }

    @Test
    void deveRetornarProjetosComSucesso() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Project> page = new PageImpl<>(List.of(project));
        var response = new ProjectResponse(1L, "Projeto Teste", "Projeto usado para teste", project.getStartDate(), null);

        when(projectRepository.findAll(pageable)).thenReturn(page);
        when(projectMapper.toResponse(project)).thenReturn(response);

        var result = projectService.buscarProjetos(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Projeto Teste", result.getContent().get(0).name());
        verify(projectRepository).findAll(pageable);
        verify(projectMapper).toResponse(project);
    }

    @Test
    void deveRetornarListaDeProjetosVaziaComSucesso() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Project> page = new PageImpl<>(List.of());

        when(projectRepository.findAll(pageable)).thenReturn(page);

        var result = projectService.buscarProjetos(pageable);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());

        verify(projectRepository).findAll(pageable);
        verify(projectMapper, never()).toResponse(any());
    }

}