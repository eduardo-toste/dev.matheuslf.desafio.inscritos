package dev.matheuslf.desafio.inscritos.exception;

public class ExistentUserException extends BusinessException {

    public ExistentUserException() {
        super("Usuário já cadastrado com este e-mail.");
    }

}
