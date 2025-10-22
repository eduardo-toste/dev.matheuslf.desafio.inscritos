package dev.matheuslf.desafio.inscritos.dto;

import dev.matheuslf.desafio.inscritos.model.enums.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;

public record UpdateTaskStatusRequest(

        @NotNull
        @Enumerated(EnumType.STRING)
        Status status

) {
}
