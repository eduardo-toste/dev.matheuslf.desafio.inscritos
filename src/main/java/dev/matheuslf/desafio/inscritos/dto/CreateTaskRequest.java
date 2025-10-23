package dev.matheuslf.desafio.inscritos.dto;

import dev.matheuslf.desafio.inscritos.model.enums.Priority;
import dev.matheuslf.desafio.inscritos.model.enums.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Date;

public record CreateTaskRequest(

        @NotBlank(message = "Titulo é obrigatório.")
        @Size(min = 5, max = 150)
        String title,

        String description,

        @Enumerated(EnumType.STRING)
        Status status,

        @Enumerated(EnumType.STRING)
        Priority priority,

        Date dueDate,

        Long projectId

) {
}
