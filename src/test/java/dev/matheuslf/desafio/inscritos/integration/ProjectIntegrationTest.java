package dev.matheuslf.desafio.inscritos.integration;

import dev.matheuslf.desafio.inscritos.dto.request.CreateProjectRequest;
import dev.matheuslf.desafio.inscritos.model.Project;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import utils.IntegrationTestBase;

import java.time.Instant;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProjectIntegrationTest extends IntegrationTestBase {

    @Test
    void deveCriarProjetoComSucesso_QuandoDadosValidos() throws Exception {
        var request = new CreateProjectRequest(
                "Projeto Teste 2",
                "Descrição de segundo projeto teste",
                Date.from(Instant.now()),
                Date.from(Instant.now().plusSeconds(900000))
        );

        mockMvc.perform(post("/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void deveRetornar400_AoCriarProjeto_QuandoDadosInvalidos() throws Exception {
        var request = new CreateProjectRequest(
                "",
                "Descrição de segundo projeto teste",
                Date.from(Instant.now()),
                Date.from(Instant.now().plusSeconds(900000))
        );

        mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveBuscarProjetosCadastradosComSucesso() throws Exception {
        projectRepository.save(new Project(
                "Projeto 1",
                "Descrição 1",
                Date.from(Instant.now()),
                Date.from(Instant.now().plusSeconds(900000))
        ));

        projectRepository.save(new Project(
                "Projeto 2",
                "Descrição 2",
                Date.from(Instant.now()),
                Date.from(Instant.now().plusSeconds(900000))
        ));

        mockMvc.perform(get("/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].name").value("Projeto 1"))
                .andExpect(jsonPath("$.content[0].description").value("Descrição 1"))
                .andExpect(jsonPath("$.content[1].name").value("Projeto 2"))
                .andExpect(jsonPath("$.content[1].description").value("Descrição 2"))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.size").value(20))
                .andExpect(jsonPath("$.number").value(0));
    }

    @Test
    void deveBuscarProjetosCadastradosComSucesso_QuandoEstiverVazio () throws Exception {
        mockMvc.perform(get("/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(0))
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.totalPages").value(0))
                .andExpect(jsonPath("$.size").value(20))
                .andExpect(jsonPath("$.number").value(0));
    }

}
