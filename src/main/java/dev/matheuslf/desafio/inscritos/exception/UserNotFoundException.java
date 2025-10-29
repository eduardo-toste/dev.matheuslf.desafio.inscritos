package dev.matheuslf.desafio.inscritos.exception;

public class UserNotFoundException extends BusinessException {

    public UserNotFoundException() {
        super("Usuário não encontrado.");
    }

}
