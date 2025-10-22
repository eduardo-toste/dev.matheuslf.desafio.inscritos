package dev.matheuslf.desafio.inscritos.exception;

public class ProjectNotFoundException extends BusinessException {

    public ProjectNotFoundException() {
        super("Projeto não encontrado.");
    }

}
