package dev.matheuslf.desafio.inscritos.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Date;

public record CreateProjectRequest(

        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 3, max = 100)
        String name,
        String description,
        Date startDate,
        Date endDate

) {
}
