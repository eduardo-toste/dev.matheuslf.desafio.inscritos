package dev.matheuslf.desafio.inscritos.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import dev.matheuslf.desafio.inscritos.dto.response.ErrorResponse;
import dev.matheuslf.desafio.inscritos.utils.ErrorBuilder;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            ProjectNotFoundException.class,
            TaskNotFoundException.class,
            UserNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFoundException(
            RuntimeException ex,
            HttpServletRequest request
    ) {
        return ErrorBuilder.build(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(ExistentUserException.class)
    public ResponseEntity<ErrorResponse> handleExistentUserException(
            ExistentUserException ex,
            HttpServletRequest request
    ) {
        return ErrorBuilder.build(HttpStatus.CONFLICT, ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        List<String> validationError = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .toList();

        return ErrorBuilder.build(HttpStatus.BAD_REQUEST, "Erro de validação", request.getRequestURI(), validationError);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex,
            HttpServletRequest request
    ) {
        Throwable causa = ex.getCause();
        if (causa instanceof InvalidFormatException ife && ife.getTargetType().isEnum()) {
            String nomeCampo = ife.getPath().isEmpty() ? "desconhecido" : ife.getPath().get(0).getFieldName();
            String valorInvalido = String.valueOf(ife.getValue());
            List<String> valoresPermitidos = Arrays.stream(ife.getTargetType().getEnumConstants())
                    .map(Object::toString)
                    .toList();

            String mensagem = String.format(
                    "Valor inválido para o campo '%s': '%s'. Valores permitidos: %s",
                    nomeCampo,
                    valorInvalido,
                    valoresPermitidos
            );

            return ErrorBuilder.build(HttpStatus.BAD_REQUEST, mensagem, request.getRequestURI());
        }
        return ErrorBuilder.build(
                HttpStatus.BAD_REQUEST,
                "Erro ao ler o corpo da requisição. Verifique o formato dos dados enviados.",
                request.getRequestURI()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleErroInterno(
            Exception ex,
            HttpServletRequest request
    ) {
        return ErrorBuilder.build(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request.getRequestURI());
    }

}
