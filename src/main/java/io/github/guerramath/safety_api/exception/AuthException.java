package io.github.guerramath.safety_api.exception;

/**
 * Excecao para erros de autenticacao.
 */
public class AuthException extends RuntimeException {

    public AuthException(String message) {
        super(message);
    }

    public AuthException(String message, Throwable cause) {
        super(message, cause);
    }
}
