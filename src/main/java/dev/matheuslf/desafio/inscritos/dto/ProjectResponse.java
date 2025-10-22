package dev.matheuslf.desafio.inscritos.dto;

import dev.matheuslf.desafio.inscritos.model.Project;
import org.springframework.data.domain.Page;

import java.util.Date;

public record ProjectResponse(

        Long id,
        String name,
        String description,
        Date startDate,
        Date endDate

) {

}
