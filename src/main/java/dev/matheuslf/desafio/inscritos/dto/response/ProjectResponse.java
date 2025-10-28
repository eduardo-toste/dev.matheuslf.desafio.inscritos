package dev.matheuslf.desafio.inscritos.dto.response;

import java.util.Date;

public record ProjectResponse(

        Long id,
        String name,
        String description,
        Date startDate,
        Date endDate

) {

}
