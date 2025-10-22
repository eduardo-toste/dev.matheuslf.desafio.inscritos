package dev.matheuslf.desafio.inscritos.dto;

import dev.matheuslf.desafio.inscritos.model.enums.Priority;
import dev.matheuslf.desafio.inscritos.model.enums.Status;

import java.util.Date;

public record TaskResponse(

        Long id,
        String title,
        String description,
        Status status,
        Priority priority,
        Date dueDate,
        Long projectId

) {
}
