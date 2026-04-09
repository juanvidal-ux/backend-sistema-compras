package br.com.seuprojeto.sistemacompras.model;

// Uma Exceção personalizada para os nossos erros de validação (Plano V6.0)
public class MigracaoValidationException extends RuntimeException {
    public MigracaoValidationException(String message) {
        super(message);
    }
}