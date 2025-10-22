package dev.matheuslf.desafio.inscritos.exception;

public class TaskNotFoundException extends BusinessException {

    public TaskNotFoundException() {
        super("Task não encontrada.");
    }

}
