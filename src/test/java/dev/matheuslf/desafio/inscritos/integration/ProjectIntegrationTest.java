package dev.matheuslf.desafio.inscritos.integration;

import dev.matheuslf.desafio.inscritos.dto.request.CreateProjectRequest;
import dev.matheuslf.desafio.inscritos.model.Project;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import utils.IntegrationTestBase;

import java.time.Instant;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
                        .header("Authorization", "Bearer " + token)
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
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveBuscarProjetosCadastradosComSucesso() throws Exception {
        projectRepository.save(new Project(
                "Projeto 1", "Descrição 1",
                Date.from(Instant.now()),
                Date.from(Instant.now().plusSeconds(900000))
        ));

        projectRepository.save(new Project(
                "Projeto 2", "Descrição 2",
                Date.from(Instant.now()),
                Date.from(Instant.now().plusSeconds(900000))
        ));

        mockMvc.perform(get("/projects")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].name").value("Projeto 1"))
                .andExpect(jsonPath("$.content[1].name").value("Projeto 2"));
    }

    @Test
    void deveBuscarProjetosVazioComSucesso() throws Exception {
        mockMvc.perform(get("/projects")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(0));
    }
}