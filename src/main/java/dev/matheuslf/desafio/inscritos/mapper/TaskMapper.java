package dev.matheuslf.desafio.inscritos.mapper;

import dev.matheuslf.desafio.inscritos.dto.request.CreateTaskRequest;
import dev.matheuslf.desafio.inscritos.dto.response.TaskResponse;
import dev.matheuslf.desafio.inscritos.model.Project;
import dev.matheuslf.desafio.inscritos.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "request.description", target = "description")
    Task toEntity(CreateTaskRequest request, Project project);

    @Mapping(source = "project.id", target = "projectId")
    TaskResponse toResponse(Task task);
}
