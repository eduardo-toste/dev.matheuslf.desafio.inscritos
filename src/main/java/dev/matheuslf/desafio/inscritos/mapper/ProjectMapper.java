package dev.matheuslf.desafio.inscritos.mapper;

import dev.matheuslf.desafio.inscritos.dto.request.CreateProjectRequest;
import dev.matheuslf.desafio.inscritos.dto.response.ProjectResponse;
import dev.matheuslf.desafio.inscritos.model.Project;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);

    ProjectResponse toResponse(Project project);

    Project toEntity(CreateProjectRequest request);

}
