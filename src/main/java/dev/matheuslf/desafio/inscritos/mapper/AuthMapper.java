package dev.matheuslf.desafio.inscritos.mapper;

import dev.matheuslf.desafio.inscritos.dto.response.LoginResponse;
import dev.matheuslf.desafio.inscritos.dto.response.RegisterUserResponse;
import dev.matheuslf.desafio.inscritos.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    AuthMapper INSTANCE = Mappers.getMapper(AuthMapper.class);

    RegisterUserResponse toResponse(User user);

    LoginResponse toResponse(String token);

}
